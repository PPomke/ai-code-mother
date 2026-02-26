package com.griffin.aicodemother.core;

import com.griffin.aicodemother.ai.AiCodeGeneratorService;
import com.griffin.aicodemother.ai.AiCodeGeneratorServiceFactory;
import com.griffin.aicodemother.ai.model.HtmlCodeResult;
import com.griffin.aicodemother.ai.model.MultiFileCodeResult;
import com.griffin.aicodemother.core.parser.CodeParserExecutor;
import com.griffin.aicodemother.core.saver.CodeFileSaverExecutor;
import com.griffin.aicodemother.exception.BusinessException;
import com.griffin.aicodemother.exception.ErrorCode;
import com.griffin.aicodemother.model.enums.CodeGenTypeEnum;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.regex.Pattern;

/**
 *
 * @className: AiCodeGeneratorFacade
 * @author: Griffin Wang
 * @date: 2026/2/24 14:50
 */
@Service
@Slf4j
public class AiCodeGeneratorFacade {
    private static final Pattern HTML_CODE_BLOCK_PATTERN = Pattern.compile("```html\\s*\\n", Pattern.CASE_INSENSITIVE);
    private static final Pattern CSS_CODE_BLOCK_PATTERN = Pattern.compile("```css\\s*\\n", Pattern.CASE_INSENSITIVE);
    private static final Pattern JS_CODE_BLOCK_PATTERN = Pattern.compile("```(?:js|javascript)\\s*\\n", Pattern.CASE_INSENSITIVE);

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;

    /**
     * 统一入口：根据类型生成并保存代码
     * @param userMessage 用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum,Long appId){
        if (codeGenTypeEnum == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"生成类型为空");
        }
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(htmlCodeResult, CodeGenTypeEnum.HTML,appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(multiFileCodeResult, CodeGenTypeEnum.MULTI_FILE,appId);
            }
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的生成类型");
        };
    }

    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(result, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(result, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 通用流式代码处理方法
     *
     * @param codeStream  代码流
     * @param codeGenType 代码生成类型
     * @return 流式响应
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenType, Long appId) {
        StringBuilder codeBuilder = new StringBuilder();
        // 实时收集代码片段
        return codeStream.doOnNext(codeBuilder::append).doOnComplete(() -> {
            // 流式返回完成后保存代码
            try {
                String completeCode = codeBuilder.toString();
                if (StrUtil.isBlank(completeCode)) {
                    log.info("流式内容为空，跳过保存，appId: {}", appId);
                    return;
                }
                if (!containsCodeBlock(completeCode, codeGenType)) {
                    log.info("未检测到代码块，跳过保存，appId: {}, codeGenType: {}", appId, codeGenType);
                    return;
                }
                // 使用执行器解析代码
                Object parsedResult = CodeParserExecutor.executeParser(completeCode, codeGenType);
                if (!hasSavableContent(parsedResult, codeGenType)) {
                    log.info("解析结果无可保存内容，跳过保存，appId: {}, codeGenType: {}", appId, codeGenType);
                    return;
                }
                // 使用执行器保存代码
                File savedDir = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenType,appId);
                log.info("保存成功，路径为：{}", savedDir.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存失败: {}", e.getMessage());
            }
        });
    }

    private boolean containsCodeBlock(String content, CodeGenTypeEnum codeGenType) {
        if (codeGenType == CodeGenTypeEnum.HTML) {
            return HTML_CODE_BLOCK_PATTERN.matcher(content).find();
        }
        if (codeGenType == CodeGenTypeEnum.MULTI_FILE) {
            return HTML_CODE_BLOCK_PATTERN.matcher(content).find()
                    || CSS_CODE_BLOCK_PATTERN.matcher(content).find()
                    || JS_CODE_BLOCK_PATTERN.matcher(content).find();
        }
        return false;
    }

    private boolean hasSavableContent(Object parsedResult, CodeGenTypeEnum codeGenType) {
        if (codeGenType == CodeGenTypeEnum.HTML) {
            HtmlCodeResult htmlCodeResult = (HtmlCodeResult) parsedResult;
            return htmlCodeResult != null && StrUtil.isNotBlank(htmlCodeResult.getHtmlCode());
        }
        if (codeGenType == CodeGenTypeEnum.MULTI_FILE) {
            MultiFileCodeResult multiFileCodeResult = (MultiFileCodeResult) parsedResult;
            return multiFileCodeResult != null
                    && (StrUtil.isNotBlank(multiFileCodeResult.getHtmlCode())
                    || StrUtil.isNotBlank(multiFileCodeResult.getCssCode())
                    || StrUtil.isNotBlank(multiFileCodeResult.getJsCode()));
        }
        return false;
    }

}

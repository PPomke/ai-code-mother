package com.griffin.aicodemother.core;

import com.griffin.aicodemother.ai.AiCodeGeneratorService;
import com.griffin.aicodemother.ai.model.HtmlCodeResult;
import com.griffin.aicodemother.ai.model.MultiFileCodeResult;
import com.griffin.aicodemother.exception.BusinessException;
import com.griffin.aicodemother.exception.ErrorCode;
import com.griffin.aicodemother.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 *
 * @className: AiCodeGeneratorFacade
 * @author: Griffin Wang
 * @date: 2026/2/24 14:50
 */
@Service
@Slf4j
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 统一入口：根据类型生成并保存代码
     * @param userMessage 用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum){
        if (codeGenTypeEnum == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"生成类型为空");
        }
        switch (codeGenTypeEnum){
            case HTML:
                return generateAndSaveHtmlCode(userMessage);
            case MULTI_FILE:
                return generateAndSaveMultiFileCode(userMessage);
            default:
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的生成类型");
        }
    }

    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeGenTypeEnum) {
            case HTML -> generateAndSaveHtmlCodeStream(userMessage);
            case MULTI_FILE -> generateAndSaveMultiFileCodeStream(userMessage);
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }


    /**
     * 生成 HTML 模式的代码并保存
     * @param userMessage 用户提示词
     * @return 保存后的目录
     */
    private File generateAndSaveHtmlCode(String userMessage) {
        HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
    }

    /**
     * 生成多文件模式的代码并保存
     * @param userMessage 用户提示词
     * @return 保存后的目录
     */
    private File generateAndSaveMultiFileCode(String userMessage) {
        MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        return CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
    }

    /**
     * 生成 HTML 模式的代码并保存 (流式)
     * @param userMessage 用户提示词
     * @return 保存后的目录
     */
    private Flux<String> generateAndSaveHtmlCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
        StringBuilder code = new StringBuilder();
        return result.doOnNext(code::append).doOnComplete(() -> {
            try {
                // 保存代码
                String completeHtmlCode = code.toString();
                HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(completeHtmlCode);

                File file = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
                log.info("保存的目录: {}", file.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存失败：{}", e.getMessage());
            }
        });
    }

    /**
     * 生成多文件模式的代码并保存 (流式)
     * @param userMessage 用户提示词
     * @return 保存后的目录
     */
    private Flux<String> generateAndSaveMultiFileCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
        StringBuilder code = new StringBuilder();
        return result.doOnNext(chunk -> {
            code.append(chunk);
        }).doOnComplete(() -> {
            try {
                // 保存代码
                String completeMultiFileCode = code.toString();
                MultiFileCodeResult multiFileCodeResult = CodeParser.parseMultiFileCode(completeMultiFileCode);

                File file = CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
                log.info("保存的目录: {}", file.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存失败：{}", e.getMessage());
            }
        });
    }
}

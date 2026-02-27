package com.griffin.aicodemother.ai;

import com.griffin.aicodemother.ai.model.HtmlCodeResult;
import com.griffin.aicodemother.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/**
 *
 * @className: AiCodeGeneratorService
 * @author: Griffin Wang
 * @date: 2026/2/24 11:15
 */
public interface AiCodeGeneratorService {

    /**
     * 生成HTML代码
     * @param userMessage 用户输入
     * @return HTML代码
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    /**
     * 生成多文件代码
     * @param userMessage 用户输入
     * @return 多文件代码
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMessage);

    /**
     * 生成HTML代码 (流式)
     * @param userMessage 用户输入
     * @return HTML代码
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(String userMessage);

    /**
     * 生成多文件代码 (流式)
     * @param userMessage 用户输入
     * @return 多文件代码
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(String userMessage);

    /**
     * 生成 Vue 项目代码（流式）
     *
     * @param userMessage 用户消息
     * @return 生成过程的流式响应
     */
    @SystemMessage(fromResource = "prompt/codegen-vue-project-system-prompt.txt")
    Flux<String> generateVueProjectCodeStream(@MemoryId long appId, @UserMessage String userMessage);

}

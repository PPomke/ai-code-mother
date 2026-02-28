package com.griffin.aicodemother.ai;

import com.griffin.aicodemother.model.enums.CodeGenTypeEnum;
import dev.langchain4j.service.SystemMessage;

/**
 *
 * @className: AiCodeGenTypeRoutingService
 * @author: Griffin Wang
 * @date: 2026/2/28 17:24
 * @description: AI代码生成类型路由服务
 */
public interface AiCodeGenTypeRoutingService {

    /**
     * 根据用户需求只能选择代码生成类型
     * @param userPrompt 用户提示词
     * @return 推荐的代码类型
     */
    @SystemMessage(fromResource = "prompt/codegen-routing-system-prompt.txt")
    CodeGenTypeEnum routeCodeGenType(String userPrompt);

}

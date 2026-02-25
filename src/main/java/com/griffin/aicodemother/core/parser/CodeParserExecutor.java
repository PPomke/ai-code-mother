package com.griffin.aicodemother.core.parser;

import com.griffin.aicodemother.exception.BusinessException;
import com.griffin.aicodemother.exception.ErrorCode;
import com.griffin.aicodemother.model.enums.CodeGenTypeEnum;

/**
 *
 * @className: CodeParserExecutor
 * @author: Griffin Wang
 * @date: 2026/2/25 13:55
 * @description: 代码解析执行器 根据代码生成类型执行相应的解析逻辑
 */
public class CodeParserExecutor {

    private static final HtmlCodeParser HTML_CODE_PARSER = new HtmlCodeParser();
    private static final MultiFileCodeParser MULTI_FILE_CODE_PARSER = new MultiFileCodeParser();

    /**
     * 根据代码生成类型执行相应的解析逻辑
     * @param codeContent 代码内容
     * @param codeGenTypeEnum 代码生成类型
     * @return 解析结果
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenTypeEnum) {
        return switch (codeGenTypeEnum) {
            case HTML -> HTML_CODE_PARSER.parserCode(codeContent);
            case MULTI_FILE -> MULTI_FILE_CODE_PARSER.parserCode(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的生成类型");
        };
    }
}

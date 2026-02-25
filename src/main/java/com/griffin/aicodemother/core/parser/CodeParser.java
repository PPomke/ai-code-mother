package com.griffin.aicodemother.core.parser;

/**
 *
 * @className: CodeParser
 * @author: Griffin Wang
 * @date: 2026/2/25 13:49
 */
public interface CodeParser<T> {
    /**
     * 解析代码内容
     * @param codeContent 代码内容
     * @return 解析结果
     */
    T parserCode(String codeContent);
}

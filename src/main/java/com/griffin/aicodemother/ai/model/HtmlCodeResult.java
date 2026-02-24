package com.griffin.aicodemother.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 *
 * @className: HtmlCodeResult
 * @author: Griffin Wang
 * @date: 2026/2/24 14:19
 */
@Data
@Description("生成 HTML 代码文件的结果")
public class HtmlCodeResult {

    @Description("HTML代码")
    private String htmlCode;

    @Description("生成代码的描述")
    private String description;
}

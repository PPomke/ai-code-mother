package com.griffin.aicodemother.core.saver;

import com.griffin.aicodemother.ai.model.HtmlCodeResult;
import com.griffin.aicodemother.model.enums.CodeGenTypeEnum;


/**
 *
 * @className: HtmlCodeFileSaverTemplate
 * @author: Griffin Wang
 * @date: 2026/2/25 14:08
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult>{
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String dirPath) {
        writeToFile(dirPath, "index.html", result.getHtmlCode());
    }

}

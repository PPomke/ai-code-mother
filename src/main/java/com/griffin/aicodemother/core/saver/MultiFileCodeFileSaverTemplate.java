package com.griffin.aicodemother.core.saver;

import com.griffin.aicodemother.ai.model.MultiFileCodeResult;
import com.griffin.aicodemother.model.enums.CodeGenTypeEnum;


/**
 *
 * @className: MultiFileCodeFileSaverTemplate
 * @author: Griffin Wang
 * @date: 2026/2/25 14:11
 */
public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult> {
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFiles(MultiFileCodeResult multiFileCodeResult, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", multiFileCodeResult.getHtmlCode());
        writeToFile(baseDirPath, "style.css", multiFileCodeResult.getCssCode());
        writeToFile(baseDirPath, "script.js", multiFileCodeResult.getJsCode());
    }
}

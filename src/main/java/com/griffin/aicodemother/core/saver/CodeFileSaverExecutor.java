package com.griffin.aicodemother.core.saver;

import com.griffin.aicodemother.ai.model.HtmlCodeResult;
import com.griffin.aicodemother.ai.model.MultiFileCodeResult;
import com.griffin.aicodemother.exception.BusinessException;
import com.griffin.aicodemother.exception.ErrorCode;
import com.griffin.aicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 *
 * @className: CodeFileSaverExecutor
 * @author: Griffin Wang
 * @date: 2026/2/25 14:13
 * @description: 代码文件保存执行器
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaver = new MultiFileCodeFileSaverTemplate();

    /**
     * 执行代码保存
     *
     * @param codeResult  代码结果对象
     * @param codeGenType 代码生成类型
     * @return 保存的目录
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType,Long appId) {
        return switch (codeGenType) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult, appId);
            case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult, appId);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType);
        };
    }
}


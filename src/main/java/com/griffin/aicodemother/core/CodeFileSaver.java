package com.griffin.aicodemother.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.griffin.aicodemother.ai.model.HtmlCodeResult;
import com.griffin.aicodemother.ai.model.MultiFileCodeResult;
import com.griffin.aicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 *
 * @className: CodeFileSaver
 * @author: Griffin Wang
 * @date: 2026/2/24 14:41
 */
public class CodeFileSaver {

    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";


    /**
     * 保存HtmlCodeResult
     * @param htmlCodeResult htmlCodeResult
     * @return 保存后的文件
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult){
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        writeToFile(baseDirPath, "index.html", htmlCodeResult.getHtmlCode());
        return new File(baseDirPath);
    }

    /**
     * 保存MultiFileCodeResult
     * @param multiFileCodeResult multiFileCodeResult
     * @return 保存后的文件
     */
    public static File saveMultiFileCodeResult(MultiFileCodeResult multiFileCodeResult){
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        writeToFile(baseDirPath, "index.html", multiFileCodeResult.getHtmlCode());
        writeToFile(baseDirPath, "style.css", multiFileCodeResult.getCssCode());
        writeToFile(baseDirPath, "script.js", multiFileCodeResult.getJsCode());
        return new File(baseDirPath);
    }

    /**
     * 构建唯一目录路径：tmp/code_output/bizType_雪花ID
     */
    private static String buildUniqueDir(String bizType){
        String uniqueDirName = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextIdStr());
        String dirpath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirpath);
        return dirpath;
    }

    /**
     * 写入单个文件
     */
    private static void writeToFile(String dirPath,String fileName,String content) {
        String filePath = dirPath + File.separator + fileName;
        FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
    }
}

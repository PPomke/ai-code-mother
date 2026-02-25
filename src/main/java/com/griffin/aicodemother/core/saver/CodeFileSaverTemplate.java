package com.griffin.aicodemother.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.griffin.aicodemother.exception.BusinessException;
import com.griffin.aicodemother.exception.ErrorCode;
import com.griffin.aicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 *
 * @className: CodeFileSaverTemplate
 * @author: Griffin Wang
 * @date: 2026/2/25 13:59
 * @description: 抽象代码文件保存器 - 模板方法
 */
public abstract class CodeFileSaverTemplate<T> {

    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    public final File saveCode(T result,Long appId) {
        // 校验输入
        validateInput(result);
        // 构建唯一目录
        String baseDirPath = buildUniqueDir(appId);
        // 保存文件
        saveFiles(result, baseDirPath);
        // 返回目录结构对象
        return new File(baseDirPath);
    }

    /**
     * 校验输入
     * @param result 输入结果
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码结果对象不能为空");
        }
    }

    /**
     * 构建唯一目录路径：tmp/code_output/bizType_雪花ID
     */
    protected final String buildUniqueDir(Long appId){
        if (appId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        }
        String codeType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", codeType, appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 获取代码类型
     * @return 代码类型
     */
    protected abstract CodeGenTypeEnum getCodeType();

    /**
     * 保存文件
     * @param result 输入结果
     * @param dirPath 目录路径
     */
    protected abstract void saveFiles(T result, String dirPath);

    /**
     * 写入单个文件
     */
    protected final void writeToFile(String dirPath,String fileName,String content) {
        String filePath = dirPath + File.separator + fileName;
        FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
    }
}

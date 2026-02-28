package com.griffin.aicodemother.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.griffin.aicodemother.exception.ErrorCode;
import com.griffin.aicodemother.exception.ThrowUtils;
import com.griffin.aicodemother.manager.CosManager;
import com.griffin.aicodemother.service.ScreenshotService;
import com.griffin.aicodemother.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @className: ScreenshotServiceImpl
 * @author: Griffin Wang
 * @date: 2026/2/28 13:59
 * @description: 截图服务实现类
 */
@Service
@Slf4j
public class ScreenshotServiceImpl implements ScreenshotService {

    @Resource
    private CosManager cosManager;

    @Override
    public String generateAndUploadScreenshot(String webUrl) {
        // 生成截图
        ThrowUtils.throwIf(StrUtil.isBlank(webUrl), ErrorCode.PARAMS_ERROR,"网页URL不能为空");
        // 上传到COS
        log.info("开始生成网页截图，URL：{}",webUrl);
        String localScreenshotPath = WebScreenshotUtils.saveWebPageScreenshot(webUrl);
        ThrowUtils.throwIf(StrUtil.isBlank(localScreenshotPath),ErrorCode.OPERATION_ERROR,"本地截图生成失败");
        try {
            String cosUrl = uploadScreenshotToCos(localScreenshotPath);
            ThrowUtils.throwIf(StrUtil.isBlank(cosUrl),ErrorCode.OPERATION_ERROR,"上传截图到COS失败");
            log.info("截图上传成功，COS文件路径：{}",cosUrl);
            return cosUrl;
        } finally {
            // 清理本地文件
            cleanupLocalFile(localScreenshotPath);
        }
    }

    /**
     * 上传截图到COS
     * @param localScreenshotPath 本地文件路径
     * @return COS文件路径
     */
    private String uploadScreenshotToCos(String localScreenshotPath) {
        if (StrUtil.isBlank(localScreenshotPath)) {
            return null;
        }
        File file = new File(localScreenshotPath);
        if (!file.exists()){
            log.error("截图文件不存在: {}", localScreenshotPath);
            return null;
        }
        // 生成COS对象键
        String fileName = UUID.randomUUID().toString().substring(0, 8) + "_compressed.jpg";
        String cosKey = generateScreenshotKey(fileName);
        return cosManager.uploadFile(cosKey, file);

    }

    /**
     * 生成COS对象键
     * @param fileName 文件名
     * @return COS对象键
     */
    private String generateScreenshotKey(String fileName) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("/screenshots/%s/%s", datePath, fileName);
    }

    /**
     * 清理本地文件
     * @param localScreenshotPath 本地文件路径
     */
    private void cleanupLocalFile(String localScreenshotPath){
        File file = new File(localScreenshotPath);
        if (file.exists()){
            File parentFile = file.getParentFile();
            FileUtil.del(parentFile);
            log.info("本地截图文件已清理: {}", localScreenshotPath);
        }
    }
}

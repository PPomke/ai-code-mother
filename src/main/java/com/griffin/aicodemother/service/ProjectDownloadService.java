package com.griffin.aicodemother.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @className: ProjectDownloadService
 * @author: Griffin Wang
 * @date: 2026/2/28 16:04
 * @description: 项目下载服务接口
 */
public interface ProjectDownloadService {

    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse  response);
}

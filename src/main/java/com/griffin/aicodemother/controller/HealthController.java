package com.griffin.aicodemother.controller;

import com.griffin.aicodemother.common.BaseResponse;
import com.griffin.aicodemother.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @className: HealthController
 * @author: Griffin Wang
 * @date: 2026/2/11 10:19
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("ok");
    }
}


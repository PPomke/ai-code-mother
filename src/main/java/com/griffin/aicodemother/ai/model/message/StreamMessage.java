package com.griffin.aicodemother.ai.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @className: StreamMessage
 * @author: Griffin Wang
 * @date: 2026/2/27 13:37
 * @description: 流式消息响应基类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamMessage {
    private String type;
}


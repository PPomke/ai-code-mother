package com.griffin.aicodemother.ai.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @className: AiResponseMessage
 * @author: Griffin Wang
 * @date: 2026/2/27 13:38
 * @description: AI 响应消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AiResponseMessage extends StreamMessage {

    private String data;

    public AiResponseMessage(String data) {
        super(StreamMessageTypeEnum.AI_RESPONSE.getValue());
        this.data = data;
    }
}


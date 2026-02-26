package com.griffin.aicodemother.service;

import com.griffin.aicodemother.model.dto.chathistory.ChatHistoryQueryRequest;
import com.griffin.aicodemother.model.entity.ChatHistory;
import com.griffin.aicodemother.model.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;


/**
 * 对话历史 服务层。
 *
 * @author griffin
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 添加对话消息
     * @param appId 应用id
     * @param userId 用户id
     * @param message 消息
     * @param messageType 消息类型
     * @return 是否成功
     */
    boolean addChatMessage(Long appId, Long userId, String message, String messageType);

    boolean deleteByAppId(Long appId);

    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize, LocalDateTime lastCreateTime, User loginUser);

    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory messageWindowChatMemory,int maxCount);

}

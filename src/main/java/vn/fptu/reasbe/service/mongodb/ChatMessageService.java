package vn.fptu.reasbe.service.mongodb;

import java.util.List;

import vn.fptu.reasbe.model.mongodb.ChatMessage;

/**
 *
 * @author dungnguyen
 */
public interface ChatMessageService {
    void processMessage(ChatMessage chatMessage);
    List<ChatMessage> findChatMessages(String senderId, String recipientId);
    List<ChatMessage> getConversationsOfUser(String username);
}

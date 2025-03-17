package vn.fptu.reasbe.repository.mongodb;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import vn.fptu.reasbe.model.mongodb.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);
    List<ChatMessage> findByChatIdContainingIgnoreCase(String username);
}

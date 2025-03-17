package vn.fptu.reasbe.service.mongodb;

import java.util.Optional;

/**
 *
 * @author dungnguyen
 */
public interface ChatRoomService {
    Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists);
    String createChatId(String senderId, String recipientId);
}

package vn.fptu.reasbe.repository.mongodb;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import vn.fptu.reasbe.model.mongodb.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}

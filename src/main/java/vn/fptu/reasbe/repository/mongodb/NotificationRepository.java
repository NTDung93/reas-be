package vn.fptu.reasbe.repository.mongodb;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import vn.fptu.reasbe.model.mongodb.Notification;

/**
 *
 * @author dungnguyen
 */
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByRecipientId(String recipientId);
}

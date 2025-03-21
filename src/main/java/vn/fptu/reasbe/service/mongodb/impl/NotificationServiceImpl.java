package vn.fptu.reasbe.service.mongodb.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.mongodb.Notification;
import vn.fptu.reasbe.repository.mongodb.NotificationRepository;
import vn.fptu.reasbe.service.mongodb.NotificationService;

/**
 *
 * @author dungnguyen
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(Notification notification) {
        validateNotification(notification);
        return notificationRepository.save(notification);
    }

    private void validateNotification(Notification notification) {
        if (notification.getRecipientId() == null || notification.getRecipientId().isEmpty()) {
            throw new IllegalArgumentException("error.notification.required.recipientId");
        }
        if (notification.getContent() == null || notification.getContent().isEmpty()) {
            throw new IllegalArgumentException("error.notification.required.content");
        }
        if (notification.getContentType() == null || notification.getContentType().isEmpty()) {
            throw new IllegalArgumentException("error.notification.required.contentType");
        }
        if (notification.getNotificationType() == null || notification.getNotificationType().isEmpty()) {
            throw new IllegalArgumentException("error.notification.required.notificationType");
        }
        if (notification.getTimestamp() == null) {
            throw new IllegalArgumentException("error.notification.required.timestamp");
        }
    }
}

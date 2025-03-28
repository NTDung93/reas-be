package vn.fptu.reasbe.service.mongodb.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;

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

    private static final Logger logger = Logger.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public Notification createNotification(Notification notification) {
        validateNotification(notification);
        return notificationRepository.save(notification);
    }

    @Override
    public BatchResponse sendNotification(Notification notification) {
        List<String> registrationTokens=notification.getRegistrationTokens();
        com.google.firebase.messaging.Notification firebaseNotification = com.google.firebase.messaging.Notification.builder()
                .setTitle(notification.getContentType())
                .setBody(notification.getContent())
                .build();

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(registrationTokens)
                .setNotification(firebaseNotification)
                .putAllData(notification.getData())
                .build();

        BatchResponse batchResponse = null;
        try {
            batchResponse = firebaseMessaging.sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            logger.info( "FCM error: " + e.getMessage());
        }
        if (batchResponse != null && batchResponse.getFailureCount() > 0) {
            List<SendResponse> responses = batchResponse.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    failedTokens.add(registrationTokens.get(i));
                }
            }
            logger.info("List of tokens that caused failures: " + failedTokens);
        }
        return batchResponse;
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

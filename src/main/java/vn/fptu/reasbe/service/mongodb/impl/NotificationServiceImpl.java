package vn.fptu.reasbe.service.mongodb.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.mongodb.notification.NotificationDto;
import vn.fptu.reasbe.model.enums.notification.TypeNotification;
import vn.fptu.reasbe.model.mongodb.Notification;
import vn.fptu.reasbe.repository.mongodb.NotificationRepository;
import vn.fptu.reasbe.service.mongodb.NotificationService;
import vn.fptu.reasbe.utils.mapper.NotificationMapper;

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
    private final NotificationMapper notificationMapper;

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

        notification.setData(Map.of("key1", "value1"));

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

    @Override
    public List<Notification> findAllByUsername(String username) {
        return notificationRepository.findByRecipientId(username);
    }

    @Override
    public List<NotificationDto> getNotificationsOfUserGroupingByType(String username) {
        List<Notification> notifications = notificationRepository.findByRecipientId(username);
        List<Notification> groupedNotifications = new ArrayList<>();

        // Group notifications by their notification type
        Map<TypeNotification, List<Notification>> notificationsByType = notifications.stream()
                .collect(Collectors.groupingBy(Notification::getNotificationType));

        for (Map.Entry<TypeNotification, List<Notification>> entry : notificationsByType.entrySet()) {
            TypeNotification type = entry.getKey();
            List<Notification> notificationsOfType = entry.getValue();

            if (type == TypeNotification.CHAT_MESSAGE) {
                Map<String, Notification> latestChatNotifications = notificationsOfType.stream()
                        .collect(Collectors.toMap(
                                Notification::getSenderId, // use senderId as the key
                                Function.identity(), // keep the notification itself as the value
                                (n1, n2) -> n1.getTimestamp().after(n2.getTimestamp()) ? n1 : n2 // if duplicate key, keep the latest notification
                        ));
                groupedNotifications.addAll(latestChatNotifications.values());
            } else {
                // For other types, add all notifications
                groupedNotifications.addAll(notificationsOfType);
            }
        }

        // Sort the final notifications list by timestamp
        groupedNotifications.sort(Comparator.comparing(Notification::getTimestamp).reversed());

        return groupedNotifications.stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    private void validateNotification(Notification notification) {
        if (StringUtils.isBlank(notification.getSenderId())) {
            throw new IllegalArgumentException("error.notification.required.recipientId");
        }
        if (StringUtils.isBlank(notification.getContent())) {
            throw new IllegalArgumentException("error.notification.required.content");
        }
        if (StringUtils.isBlank(notification.getContentType())) {
            throw new IllegalArgumentException("error.notification.required.contentType");
        }
        if (notification.getNotificationType() == null) {
            throw new IllegalArgumentException("error.notification.required.notificationType");
        }
        if (notification.getTimestamp() == null) {
            throw new IllegalArgumentException("error.notification.required.timestamp");
        }
    }
}

package vn.fptu.reasbe.service.mongodb;

import com.google.firebase.messaging.BatchResponse;

import vn.fptu.reasbe.model.mongodb.Notification;

/**
 *
 * @author dungnguyen
 */
public interface NotificationService {
    Notification createNotification(Notification notification);
    BatchResponse sendNotification(Notification notification);
}

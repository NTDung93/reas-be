package vn.fptu.reasbe.service.mongodb;

import java.util.List;

import com.google.firebase.messaging.BatchResponse;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.mongodb.notification.NotificationDto;
import vn.fptu.reasbe.model.mongodb.Notification;

/**
 *
 * @author dungnguyen
 */
public interface NotificationService {
    Notification createNotification(Notification notification);
    BatchResponse sendNotification(Notification notification);
    List<Notification> findAllByUsername(String username);
    BaseSearchPaginationResponse<NotificationDto> getNotificationsOfUserGroupingByType(int pageNo, int pageSize, String username);
    void saveAndSendNotification(Notification notification);
}

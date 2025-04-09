package vn.fptu.reasbe.model.dto.mongodb.notification;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.notification.TypeNotification;

/**
 *
 * @author dungnguyen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationDto {
    String senderId;
    String recipientId;
    String content;
    Date timestamp;
    String contentType;
    TypeNotification notificationType;
}

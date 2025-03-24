package vn.fptu.reasbe.model.mongodb;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    String id;
    String senderId;
    String recipientId;
    String content;
    Date timestamp;
    String contentType;
    String notificationType;
}

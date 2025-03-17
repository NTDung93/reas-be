package vn.fptu.reasbe.model.mongodb;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    @Id
    String id;
    String chatId;
    String senderId;
    String recipientId;
    String senderName;
    String recipientName;
    String content;
    Date timestamp;
}

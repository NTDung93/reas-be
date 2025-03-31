package vn.fptu.reasbe.service.mongodb.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.mongodb.ChatMessage;
import vn.fptu.reasbe.model.mongodb.Notification;
import vn.fptu.reasbe.model.mongodb.User;
import vn.fptu.reasbe.repository.mongodb.ChatMessageRepository;
import vn.fptu.reasbe.service.mongodb.ChatMessageService;
import vn.fptu.reasbe.service.mongodb.ChatRoomService;
import vn.fptu.reasbe.service.mongodb.NotificationService;
import vn.fptu.reasbe.service.mongodb.UserMService;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private static final String QUEUE_MESSAGES = "/queue/messages";

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final NotificationService notificationService;
    private final UserMService userMService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void processMessage(ChatMessage chatMessage) {
        validateMessage(chatMessage);
        ChatMessage savedMsg = saveMessage(chatMessage);
        User recipient = userMService.findByUsername(chatMessage.getRecipientId());
        Notification savedNotification = notificationService.createNotification(prepareNotification(savedMsg, recipient));
        notificationService.sendNotification(savedNotification);
        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId(), QUEUE_MESSAGES, savedNotification);
    }

    private Notification prepareNotification(ChatMessage savedMsg, User recipient) {
        return Notification.builder()
                .id(savedMsg.getId())
                .senderId(savedMsg.getSenderId())
                .recipientId(savedMsg.getRecipientId())
                .content(savedMsg.getContent())
                .timestamp(savedMsg.getTimestamp())
                .contentType(savedMsg.getContentType())
                .notificationType("message")
                .registrationTokens(recipient.getRegistrationTokens())
                .build();
    }

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        String chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow();
        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        String chatId = chatRoomService
                .getChatRoomId(senderId, recipientId, false)
                .orElseThrow();
        return repository.findByChatId(chatId);
    }

    @Override
    public List<ChatMessage> getConversationsOfUser(String username) {
        List<ChatMessage> chatMessages = repository.findByChatIdContainingIgnoreCase(username);

        // Group messages by chatId and pick the one with the latest timestamp for each group.
        return chatMessages.stream()
                .collect(Collectors.groupingBy(
                        ChatMessage::getChatId,
                        Collectors.maxBy(Comparator.comparing(ChatMessage::getTimestamp))
                ))
                .values()
                .stream()
                .flatMap(java.util.Optional::stream)
                .toList();
    }

    private void validateMessage(ChatMessage chatMessage) {
        if (chatMessage.getSenderId() == null || chatMessage.getSenderId().isEmpty()) {
            throw new IllegalArgumentException("error.chat.required.senderId");
        }
        if (chatMessage.getRecipientId() == null || chatMessage.getRecipientId().isEmpty()) {
            throw new IllegalArgumentException("error.chat.required.recipientId");
        }
        if (chatMessage.getContent() == null || chatMessage.getContent().isEmpty()) {
            throw new IllegalArgumentException("error.chat.required.content");
        }
        if (chatMessage.getContentType() == null || chatMessage.getContentType().isEmpty()) {
            throw new IllegalArgumentException("error.chat.required.contentType");
        }
        if (chatMessage.getTimestamp() == null) {
            throw new IllegalArgumentException("error.notification.required.timestamp");
        }
    }
}

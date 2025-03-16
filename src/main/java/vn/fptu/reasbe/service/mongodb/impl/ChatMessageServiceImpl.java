package vn.fptu.reasbe.service.mongodb.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.mongodb.ChatMessage;
import vn.fptu.reasbe.model.mongodb.ChatNotification;
import vn.fptu.reasbe.repository.mongodb.ChatMessageRepository;
import vn.fptu.reasbe.service.mongodb.ChatMessageService;
import vn.fptu.reasbe.service.mongodb.ChatRoomService;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private static final String QUEUE_MESSAGES = "/queue/messages";

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void processMessage(ChatMessage chatMessage) {
        ChatMessage savedMsg = saveMessage(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), QUEUE_MESSAGES, // john/queue/messages
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
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
}

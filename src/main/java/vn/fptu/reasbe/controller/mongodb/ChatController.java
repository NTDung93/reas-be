package vn.fptu.reasbe.controller.mongodb;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.mongodb.ChatMessage;
import vn.fptu.reasbe.service.mongodb.ChatMessageService;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        chatMessageService.processMessage(chatMessage);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/conversations/{username}")
    public ResponseEntity<List<ChatMessage>> getConversationsOfUser(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(chatMessageService.getConversationsOfUser(username));
    }
}

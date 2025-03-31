package vn.fptu.reasbe.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.BatchResponse;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.mongodb.Notification;
import vn.fptu.reasbe.service.mongodb.NotificationService;

/**
 *
 * @author dungnguyen
 */
@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<Notification> getAllNotifications(@RequestParam String username) {
        return notificationService.findAllByUsername(username);
    }

    @PostMapping("/send-notification")
    public BatchResponse sendNotification(@RequestBody Notification notification){
        return notificationService.sendNotification(notification);
    }
}

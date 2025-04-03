package vn.fptu.reasbe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.usersubscription.UserSubscriptionDto;
import vn.fptu.reasbe.service.UserSubscriptionService;

/**
 *
 * @author dungnguyen
 */
@RestController
@RequestMapping("/api/v1/user-subscription")
@RequiredArgsConstructor
public class UserSubscriptionController {
    private final UserSubscriptionService userSubscriptionService;

    @GetMapping("/current-subscription")
    public ResponseEntity<UserSubscriptionDto> getCurrentSubscription() {
        return ResponseEntity.ok(userSubscriptionService.getUserCurrentSubscription());
    }
}

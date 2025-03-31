package vn.fptu.reasbe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.payos.CreatePaymentLinkRequest;
import vn.fptu.reasbe.model.dto.payos.WebhookUrlDto;
import vn.fptu.reasbe.service.PayOSService;
import vn.payos.type.CheckoutResponseData;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payos/")
public class PayOSController {

    private final PayOSService payOSService;

    @PostMapping(path = "/create-payment-link")
    @PreAuthorize("hasAnyRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    public ResponseEntity<CheckoutResponseData> createPaymentLink(@RequestBody @Valid CreatePaymentLinkRequest createPaymentLinkRequest) {
        return ResponseEntity.ok(payOSService.createPayment(createPaymentLinkRequest));
    }

    @PostMapping(path = "/confirm-webhook")
    public ResponseEntity<String> confirmWebhook(@RequestBody @Valid WebhookUrlDto webhookUrlDto) {
        return ResponseEntity.ok(payOSService.confirmWebhook(webhookUrlDto));
    }
}

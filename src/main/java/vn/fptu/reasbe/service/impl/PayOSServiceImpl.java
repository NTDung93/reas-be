package vn.fptu.reasbe.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.payos.CreatePaymentLinkRequest;
import vn.fptu.reasbe.model.dto.payos.WebhookUrlDto;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.exception.PayOSException;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.PayOSService;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

/**
 *
 * @author dungnguyen
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOSService {

    private static final long EXPIRED_TIME = 30; // minutes

    private final PayOS payOS;
    private final AuthService authService;

    @Override
    public CheckoutResponseData createPayment(CreatePaymentLinkRequest createPaymentLinkRequest) {
        User currentUser = authService.getCurrentUser();

        // Gen order code
        String currentTimeString = String.valueOf(String.valueOf(new Date().getTime()));
        long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

        // Set expiration time
        Instant now = Instant.now();
        Instant expirationTime = now.plus(Duration.ofMinutes(EXPIRED_TIME));
        long expirationTimestamp = expirationTime.getEpochSecond();

        ItemData item = ItemData.builder()
                .name(createPaymentLinkRequest.getSubscriptionPlan().getName())
                .price(createPaymentLinkRequest.getSubscriptionPlan().getPrice().toBigInteger().intValue())
                .quantity(1)
                .build();

        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .buyerName(currentUser.getFullName())
                .description(createPaymentLinkRequest.getDescription())
                .amount(createPaymentLinkRequest.getSubscriptionPlan().getPrice().toBigInteger().intValue())
                .item(item)
                .returnUrl(createPaymentLinkRequest.getReturnUrl())
                .cancelUrl(createPaymentLinkRequest.getCancelUrl())
                .expiredAt(expirationTimestamp)
                .build();

        try {
            return payOS.createPaymentLink(paymentData);
        } catch (Exception e) {
            throw new PayOSException(e.getMessage());
        }
    }

    @Override
    public String confirmWebhook(WebhookUrlDto webhookUrlDto) {
        try {
            return payOS.confirmWebhook(webhookUrlDto.getWebhookUrl());
        } catch (Exception e) {
            throw new PayOSException(e.getMessage());
        }
    }
}

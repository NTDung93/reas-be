package vn.fptu.reasbe.service.impl;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.payos.CreatePaymentLinkRequest;
import vn.fptu.reasbe.model.dto.payos.WebhookUrlDto;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.exception.PayOSException;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.PayOSService;
import vn.fptu.reasbe.service.SubscriptionPlanService;
import vn.fptu.reasbe.utils.common.PaymentCodeHelper;
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
    private final SubscriptionPlanService subscriptionPlanService;

    @Override
    public CheckoutResponseData createPayment(CreatePaymentLinkRequest createPaymentLinkRequest) {
        User currentUser = authService.getCurrentUser();

        // Create item data
        SubscriptionPlan subscriptionPlan = subscriptionPlanService.getSubscriptionPlanByPlanId(createPaymentLinkRequest.getSubscriptionPlanId());

        ItemData item = ItemData.builder()
                .name(subscriptionPlan.getName())
                .price(subscriptionPlan.getPrice().toBigInteger().intValue())
                .quantity(1)
                .build();

        // Gen order code
        String orderCode = PaymentCodeHelper.generateOrderCode(createPaymentLinkRequest.getSubscriptionPlanId());

        // Set expiration time
        Instant now = Instant.now();
        Instant expirationTime = now.plus(Duration.ofMinutes(EXPIRED_TIME));
        long expirationTimestamp = expirationTime.getEpochSecond();

        PaymentData paymentData = PaymentData.builder()
                .orderCode(Long.valueOf(orderCode))
                .buyerName(currentUser.getFullName())
                .description(createPaymentLinkRequest.getDescription())
                .amount(subscriptionPlan.getPrice().toBigInteger().intValue())
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

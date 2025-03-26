package vn.fptu.reasbe.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.model.enums.payment.MethodPayment;
import vn.fptu.reasbe.model.enums.payment.StatusPayment;
import vn.fptu.reasbe.model.exception.PayOSException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.PaymentHistoryRepository;
import vn.fptu.reasbe.service.PaymentHistoryService;
import vn.fptu.reasbe.service.UserSubscriptionService;
import vn.fptu.reasbe.utils.common.PaymentCodeHelper;
import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

/**
 *
 * @author dungnguyen
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    private final PayOS payOS;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final UserSubscriptionService userSubscriptionService;

    @Override
    public void payOsTransferHandler(ObjectNode body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);

            Integer subscriptionPlanId = Math.toIntExact(PaymentCodeHelper.getItemIdFromOrderCode(data.getOrderCode()));

            PaymentHistory paymentHistory = PaymentHistory.builder()
                    .transactionId(data.getOrderCode())
                    .amount(BigDecimal.valueOf(data.getAmount()))
                    .description(data.getDescription())
                    .transactionDateTime(LocalDateTime.parse(data.getTransactionDateTime()))
                    .statusPayment(Boolean.TRUE.equals(webhookBody.getSuccess()) ? StatusPayment.SUCCESS : StatusPayment.FAILED)
                    .methodPayment(MethodPayment.BANK_TRANSFER)
                    .build();
            PaymentHistory savedPaymentHistory = paymentHistoryRepository.save(paymentHistory);

            if (Boolean.TRUE.equals(webhookBody.getSuccess())) {
                userSubscriptionService.createUserSubscription(subscriptionPlanId, savedPaymentHistory);
            }
        } catch (Exception e) {
            throw new PayOSException(e.getMessage());
        }
    }

    @Override
    public PaymentHistory getPaymentHistoryById(Integer id) {
        return paymentHistoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PaymentHistory", "id", id));
    }
}

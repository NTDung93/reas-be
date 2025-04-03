package vn.fptu.reasbe.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.paymenthistory.PaymentHistoryDto;
import vn.fptu.reasbe.model.dto.paymenthistory.SearchPaymentHistoryRequest;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;
import vn.fptu.reasbe.model.enums.payment.MethodPayment;
import vn.fptu.reasbe.model.enums.payment.StatusPayment;
import vn.fptu.reasbe.model.exception.PayOSException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.PaymentHistoryRepository;
import vn.fptu.reasbe.service.ItemService;
import vn.fptu.reasbe.service.PaymentHistoryService;
import vn.fptu.reasbe.service.SubscriptionPlanService;
import vn.fptu.reasbe.service.UserService;
import vn.fptu.reasbe.service.UserSubscriptionService;
import vn.fptu.reasbe.utils.common.PaymentCodeHelper;
import vn.fptu.reasbe.utils.mapper.PaymentHistoryMapper;
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
    private final PaymentHistoryMapper paymentHistoryMapper;
    private final UserService userService;
    private final ItemService itemService;
    private final SubscriptionPlanService subscriptionPlanService;

    @Override
    public BaseSearchPaginationResponse<PaymentHistoryDto> searchPaymentHistoryPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchPaymentHistoryRequest request) {
        return BaseSearchPaginationResponse.of(paymentHistoryRepository.searchPaymentHistoryPagination(request, BaseSearchPaginationResponse.getPageable(pageNo, pageSize, sortBy, sortDir))
                .map(paymentHistoryMapper::toDto));
    }

    @Override
    public BaseSearchPaginationResponse<PaymentHistoryDto> searchPaymentHistoryOfUserPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchPaymentHistoryRequest request, Integer userId) {
        if (userService.getUserById(userId) != null) {
            if (request == null) {
                request = new SearchPaymentHistoryRequest();
            }
            request.setUserId(userId);
            return BaseSearchPaginationResponse.of(paymentHistoryRepository.searchPaymentHistoryPagination(request, BaseSearchPaginationResponse.getPageable(pageNo, pageSize, sortBy, sortDir))
                    .map(paymentHistoryMapper::toDto));
        }
        return null;
    }

    @Override
    public void payOsTransferHandler(ObjectNode body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            Item item = null;

            Integer subscriptionPlanId = Math.toIntExact(PaymentCodeHelper.getItemIdFromOrderCode(data.getOrderCode()).getFirst());
            SubscriptionPlan subscriptionPlan = subscriptionPlanService.getSubscriptionPlanByPlanId(subscriptionPlanId);

            int itemId = Math.toIntExact(PaymentCodeHelper.getItemIdFromOrderCode(data.getOrderCode()).getSecond());
            if (itemId != 0) {
                item = itemService.getItemById(itemId);
            }

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
                userSubscriptionService.createUserSubscription(subscriptionPlan, item, savedPaymentHistory);
                if (item != null) {
                    itemService.extendItem(item, subscriptionPlan);
                }
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

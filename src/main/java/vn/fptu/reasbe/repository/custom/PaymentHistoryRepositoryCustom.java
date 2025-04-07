package vn.fptu.reasbe.repository.custom;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.fptu.reasbe.model.dto.paymenthistory.SearchPaymentHistoryRequest;
import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.model.enums.subscriptionplan.TypeSubscriptionPlan;

/**
 *
 * @author dungnguyen
 */
public interface PaymentHistoryRepositoryCustom {
    Page<PaymentHistory> searchPaymentHistoryPagination(SearchPaymentHistoryRequest request, Pageable pageable);
    BigDecimal getMonthlyRevenue(Integer month, Integer year);
    HashMap<TypeSubscriptionPlan, BigDecimal> getMonthlyRevenueBySubscriptionPlan(Integer month, Integer year);
}

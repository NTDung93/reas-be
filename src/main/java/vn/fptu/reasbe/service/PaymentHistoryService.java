package vn.fptu.reasbe.service;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.paymenthistory.PaymentHistoryDto;
import vn.fptu.reasbe.model.dto.paymenthistory.SearchPaymentHistoryRequest;
import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.model.enums.subscriptionplan.TypeSubscriptionPlan;

/**
 *
 * @author dungnguyen
 */
public interface PaymentHistoryService {
    BaseSearchPaginationResponse<PaymentHistoryDto> searchPaymentHistoryPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchPaymentHistoryRequest request);
    BaseSearchPaginationResponse<PaymentHistoryDto> searchPaymentHistoryOfUserPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchPaymentHistoryRequest request, Integer userId);
    void payOsTransferHandler(ObjectNode body) throws Exception;
    PaymentHistory getPaymentHistoryById(Integer id);
    BigDecimal getMonthlyRevenue(Integer month, Integer year);
    Integer getNumberOfSuccessfulTransaction(Integer month, Integer year);
    Integer getNumberOfSuccessfulTransactionOfUser(Integer month, Integer year);
    Map<TypeSubscriptionPlan, BigDecimal> getMonthlyRevenueBySubscriptionPlan(Integer month, Integer year);
    Map<Integer, Map<TypeSubscriptionPlan, BigDecimal>> getMonthlyRevenueBySubscriptionPlanInAYear(Integer year);
}

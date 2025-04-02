package vn.fptu.reasbe.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.fptu.reasbe.model.dto.paymenthistory.SearchPaymentHistoryRequest;
import vn.fptu.reasbe.model.entity.PaymentHistory;

/**
 *
 * @author dungnguyen
 */
public interface PaymentHistoryRepositoryCustom {
    Page<PaymentHistory> searchPaymentHistoryPagination(SearchPaymentHistoryRequest request, Pageable pageable);
}

package vn.fptu.reasbe.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.payment.StatusPayment;
import vn.fptu.reasbe.repository.custom.PaymentHistoryRepositoryCustom;

/**
 *
 * @author dungnguyen
 */
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer>, QuerydslPredicateExecutor<PaymentHistory>, PaymentHistoryRepositoryCustom {
    Integer countByTransactionDateTimeIsBetweenAndStatusPaymentAndStatusEntity(LocalDateTime from, LocalDateTime to, StatusPayment statusPayment, StatusEntity statusEntity);
    Integer countByTransactionDateTimeIsBetweenAndStatusPaymentAndUserSubscription_User_IdAndStatusEntity(LocalDateTime from, LocalDateTime to, StatusPayment statusPayment, Integer userId, StatusEntity statusEntity);
}

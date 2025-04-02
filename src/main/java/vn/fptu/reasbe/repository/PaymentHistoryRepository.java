package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.repository.custom.PaymentHistoryRepositoryCustom;

/**
 *
 * @author dungnguyen
 */
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer>, QuerydslPredicateExecutor<PaymentHistory>, PaymentHistoryRepositoryCustom {

}

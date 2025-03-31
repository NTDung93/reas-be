package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.fptu.reasbe.model.entity.PaymentHistory;

/**
 *
 * @author dungnguyen
 */
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {
}

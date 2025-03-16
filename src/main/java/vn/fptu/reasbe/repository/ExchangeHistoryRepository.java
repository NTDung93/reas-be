package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fptu.reasbe.model.entity.ExchangeHistory;

public interface ExchangeHistoryRepository extends JpaRepository<ExchangeHistory, Integer> {
}

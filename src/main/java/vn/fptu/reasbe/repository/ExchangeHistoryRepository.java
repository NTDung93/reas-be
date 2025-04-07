package vn.fptu.reasbe.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.fptu.reasbe.model.entity.ExchangeHistory;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;
import vn.fptu.reasbe.repository.custom.ExchangeHistoryRepositoryCustom;

public interface ExchangeHistoryRepository extends JpaRepository<ExchangeHistory, Integer>, QuerydslPredicateExecutor<ExchangeHistory>, ExchangeHistoryRepositoryCustom {
    Integer countByCreationDateBetweenAndStatusExchangeHistoryAndStatusEntity(LocalDateTime from, LocalDateTime to, StatusExchangeHistory statusExchangeHistory, StatusEntity statusEntity);
}

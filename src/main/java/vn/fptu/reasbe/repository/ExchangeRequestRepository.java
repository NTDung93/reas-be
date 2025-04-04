package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import vn.fptu.reasbe.model.entity.ExchangeRequest;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeRequest;
import vn.fptu.reasbe.repository.custom.ExchangeRequestRepositoryCustom;

import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Integer>, QuerydslPredicateExecutor<ExchangeRequest>, ExchangeRequestRepositoryCustom {
    List<ExchangeRequest> findAllByStatusExchangeRequestAndExchangeDateAfter(StatusExchangeRequest statusExchangeRequest, LocalDateTime currDateTime);
}

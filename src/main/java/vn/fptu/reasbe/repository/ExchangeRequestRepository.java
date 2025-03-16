package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.fptu.reasbe.model.entity.ExchangeRequest;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Integer> {
    @Query("SELECT er FROM ExchangeRequest er " +
            "WHERE er.statusExchangeRequest = :status " +
            "AND (:user IN (er.sellerItem.owner, er.buyerItem.owner, er.paidBy))")
    List<ExchangeRequest> findByExchangeRequestStatusAndUser(
            @Param("status") StatusExchangeRequest status,
            @Param("user") User user);

    @Query("SELECT er FROM ExchangeRequest er " +
            "WHERE er.exchangeHistory.statusExchangeHistory = :status " +
            "AND (:user IN (er.sellerItem.owner, er.buyerItem.owner, er.paidBy))")
    List<ExchangeRequest> findByExchangeHistoryStatusAndUser(
            @Param("status") StatusExchangeHistory status,
            @Param("user") User user);

    @Query("SELECT e FROM ExchangeRequest e " +
            "JOIN e.exchangeHistory h " +
            "WHERE e.exchangeDate < :date " +
            "AND h.statusExchangeHistory = :status")
    List<ExchangeRequest> findAllExceedingDateExchanges(@Param("date") LocalDateTime date,
                                                        @Param("status") StatusExchangeHistory status);

}

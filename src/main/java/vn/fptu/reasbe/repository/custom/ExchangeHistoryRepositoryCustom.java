package vn.fptu.reasbe.repository.custom;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author dungnguyen
 */
public interface ExchangeHistoryRepositoryCustom {
    Integer getNumberOfSuccessfulExchangesOfUser(Integer month, Integer year, Integer userId);
    BigDecimal getRevenueOfUserInOneYearFromExchanges(Integer year, Integer userId);
    Map<Integer, BigDecimal> getMonthlyRevenueOfUserInOneYearFromExchanges(Integer year, Integer userId);
}

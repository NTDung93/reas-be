package vn.fptu.reasbe.repository.custom;

/**
 *
 * @author dungnguyen
 */
public interface ExchangeHistoryRepositoryCustom {
    Integer getNumberOfSuccessfulExchangesOfUser(Integer month, Integer year, Integer userId);
}

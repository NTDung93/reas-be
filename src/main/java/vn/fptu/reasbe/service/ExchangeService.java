package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.exchange.EvidenceExchangeRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestResponse;
import vn.fptu.reasbe.model.dto.exchange.ExchangeResponse;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeService {
    List<ExchangeResponse> getAllExchangeByStatusOfCurrentUser(StatusExchangeRequest statusRequest, StatusExchangeHistory statusHistory);

    ExchangeResponse getExchangeById(Integer id);

    ExchangeRequestResponse createExchangeRequest(ExchangeRequestRequest exchangeRequestRequest);

    ExchangeRequestResponse updateExchangeRequestPrice(Integer id, BigDecimal finalPrice);

    ExchangeResponse reviewExchangeRequest(Integer id, StatusExchangeRequest statusExchangeRequest);

    ExchangeResponse cancelApprovedExchange(Integer id);

    ExchangeResponse uploadEvidence(EvidenceExchangeRequest request);
}

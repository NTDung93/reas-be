package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.exchange.ExchangeHistoryResponse;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestResponse;
import vn.fptu.reasbe.model.dto.exchange.ExchangeResponse;
import vn.fptu.reasbe.model.entity.ExchangeHistory;
import vn.fptu.reasbe.model.entity.ExchangeRequest;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                UserMapper.class,
                ItemMapper.class,
        }
)
@Component
public interface ExchangeMapper {

    ExchangeResponse toExchangeResponse(ExchangeRequest exchangeRequest);

    @Mapping(target = "finalPrice", source = "estimatePrice")
    ExchangeRequest toExchangeRequest(ExchangeRequestRequest exchangeRequest);

    ExchangeRequestResponse toExchangeRequestResponse(ExchangeRequest exchangeRequest);

    ExchangeHistoryResponse toExchangeHistoryResponse(ExchangeHistory exchangeHistory);
}

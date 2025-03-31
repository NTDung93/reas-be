package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeResponse;
import vn.fptu.reasbe.model.entity.ExchangeRequest;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                UserMapper.class,
                ItemMapper.class,
                ExchangeHistoryMapper.class,
        }
)
public interface ExchangeRequestMapper {

    @Mapping(target = "feedbackId", source = "exchangeHistory.feedback.id")
    ExchangeResponse toExchangeResponse(ExchangeRequest exchangeRequest);

    @Mapping(target = "finalPrice", source = "estimatePrice")
    ExchangeRequest toExchangeRequest(ExchangeRequestRequest exchangeRequest);

    @Mapping(target = "exchangeHistory", ignore = true)
    ExchangeResponse toExchangeRequestResponse(ExchangeRequest exchangeRequest);
}

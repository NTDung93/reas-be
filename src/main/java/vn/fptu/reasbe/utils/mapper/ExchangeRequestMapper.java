package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeResponse;
import vn.fptu.reasbe.model.entity.ExchangeRequest;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                UserMapper.class,
                ItemMapper.class,
                ExchangeHistoryMapper.class,
        }
)
@Component
public interface ExchangeRequestMapper {

    @Named("toExchangeResponse")
    @Mapping(target = "feedbackId", source = "exchangeHistory.feedback.id")
    ExchangeResponse toExchangeResponse(ExchangeRequest exchangeRequest);

    @Mapping(target = "finalPrice", source = "estimatePrice")
    ExchangeRequest toExchangeRequest(ExchangeRequestRequest exchangeRequest);

    @Named("toExchangeRequestResponse")
    @Mapping(target = "exchangeHistory", ignore = true)
    ExchangeResponse toExchangeRequestResponse(ExchangeRequest exchangeRequest);
}

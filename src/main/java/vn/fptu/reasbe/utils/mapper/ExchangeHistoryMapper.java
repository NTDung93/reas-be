package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import vn.fptu.reasbe.model.dto.exchange.ExchangeHistoryResponse;
import vn.fptu.reasbe.model.entity.ExchangeHistory;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ExchangeHistoryMapper {

    ExchangeHistoryResponse toExchangeHistoryResponse(ExchangeHistory exchangeHistory);
}

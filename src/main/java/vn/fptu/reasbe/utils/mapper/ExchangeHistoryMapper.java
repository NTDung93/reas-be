package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.exchange.ExchangeHistoryResponse;
import vn.fptu.reasbe.model.entity.ExchangeHistory;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
@Component
public interface ExchangeHistoryMapper {

    ExchangeHistoryResponse toExchangeHistoryResponse(ExchangeHistory exchangeHistory);
}

package vn.fptu.reasbe.utils.converter.exchange;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

@Converter(autoApply = true)
public class StatusExchangeHistoryConverter extends BaseEnumConverter<StatusExchangeHistory> {

    public StatusExchangeHistoryConverter() { super(StatusExchangeHistory.class); }
}
package vn.fptu.reasbe.utils.converter.exchange;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.model.enums.item.MethodExchange;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

/**
 *
 * @author ntig
 */
@Converter(autoApply = true)
public class MethodExchangeConverter extends BaseEnumConverter<MethodExchange> {

    public MethodExchangeConverter() {
        super(MethodExchange.class);
    }
}

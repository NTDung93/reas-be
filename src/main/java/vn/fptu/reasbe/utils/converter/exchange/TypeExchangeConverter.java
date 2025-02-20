package vn.fptu.reasbe.utils.converter.exchange;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.model.enums.item.TypeExchange;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

/**
 *
 * @author ntig
 */
@Converter(autoApply = true)
public class TypeExchangeConverter extends BaseEnumConverter<TypeExchange> {

    public TypeExchangeConverter() {
        super(TypeExchange.class);
    }
}

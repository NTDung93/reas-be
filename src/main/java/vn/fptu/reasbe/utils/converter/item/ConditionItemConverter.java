package vn.fptu.reasbe.utils.converter.item;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.model.enums.item.ConditionItem;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

/**
 *
 * @author ntig
 */
@Converter(autoApply = true)
public class ConditionItemConverter extends BaseEnumConverter<ConditionItem> {

    public ConditionItemConverter() {
        super(ConditionItem.class);
    }
}

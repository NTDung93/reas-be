package vn.fptu.reasbe.utils.converter.item;

import vn.fptu.reasbe.model.enums.item.StatusItem;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

/**
 *
 * @author ntig
 */
@Converter(autoApply = true)
public class StatusItemConverter extends BaseEnumConverter<StatusItem> {

    public StatusItemConverter() {
        super(StatusItem.class);
    }
}
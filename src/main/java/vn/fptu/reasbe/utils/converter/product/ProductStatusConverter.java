package vn.fptu.reasbe.utils.converter.product;

import vn.fptu.reasbe.model.enums.item.ItemStatus;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author ntig
 */
@Converter(autoApply = true)
public class ProductStatusConverter implements AttributeConverter<ItemStatus, String> {

    @Override
    public String convertToDatabaseColumn(ItemStatus addressType) {
        if (addressType == null) {
            return null;
        }
        return addressType.getCode();
    }

    @Override
    public ItemStatus convertToEntityAttribute(String data) {
        if (data == null || data.trim().isEmpty()) {
            return null;
        }
        try {
            return ItemStatus.toEnumConstant(data);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }
}


package vn.fptu.reasbe.utils.converter.product;

import vn.fptu.reasbe.model.enums.product.ProductStatus;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author ntig
 */
@Converter(autoApply = true)
public class ProductStatusConverter implements AttributeConverter<ProductStatus, String> {

    @Override
    public String convertToDatabaseColumn(ProductStatus addressType) {
        if (addressType == null) {
            return null;
        }
        return addressType.getCode();
    }

    @Override
    public ProductStatus convertToEntityAttribute(String data) {
        if (data == null || data.trim().isEmpty()) {
            return null;
        }
        try {
            return ProductStatus.toEnumConstant(data);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }
}


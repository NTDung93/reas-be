package vn.fptu.reasbe.utils.converter.category;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.model.enums.category.TypeItem;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

/**
 *
 * @author ntig
 */
@Converter(autoApply = true)
public class TypeItemConverter extends BaseEnumConverter<TypeItem> {

    public TypeItemConverter() {
        super(TypeItem.class);
    }
}

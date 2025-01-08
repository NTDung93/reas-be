package vn.fptu.reasbe.utils.converter.core;

import jakarta.persistence.AttributeConverter;
import vn.fptu.reasbe.model.enums.core.BaseEnum;

/**
 *
 * @author ntig
 */
public abstract class BaseEnumConverter<E extends Enum<E> & BaseEnum.CodeAccessible>
        implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    protected BaseEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        try {
            return BaseEnum.toEnumConstant(enumClass, dbData);
        } catch (Exception e) {
            return null; // Handle invalid database value gracefully
        }
    }
}
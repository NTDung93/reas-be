package vn.fptu.reasbe.model.enums.core;

import java.util.Arrays;

import vn.fptu.reasbe.model.exception.ResourceNotFoundException;

/**
 *
 * @author ntig
 */

public abstract class BaseEnum<T extends Enum<T> & BaseEnum.CodeAccessible> {

    public interface CodeAccessible {
        String getCode();
    }

    public static < E extends Enum<E> & CodeAccessible> E toEnumConstant(Class<E> enumClass, String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(enumClass.getSimpleName(), "code", code));
    }
}

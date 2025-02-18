package vn.fptu.reasbe.model.enums.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.enums.core.BaseEnum;

/**
 *
 * @author ntig
 */
@Getter
@RequiredArgsConstructor
public enum TypeExchange implements BaseEnum.CodeAccessible {
    EXCHANGE_WITH_DESIRED_ITEM("DESI"),
    OPEN_EXCHANGE("OPEN");

    private final String code;

    public static TypeExchange toEnumConstant(String code) {
        return BaseEnum.toEnumConstant(TypeExchange.class, code);
    }
}

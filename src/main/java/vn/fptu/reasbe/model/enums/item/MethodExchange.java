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
public enum MethodExchange implements BaseEnum.CodeAccessible {
    PICK_UP_IN_PERSON("PICK"),
    DELIVERY("DELI"),
    MEET_AT_GIVEN_LOCATION("MEET"),;

    private final String code;

    public static MethodExchange toEnumConstant(String code) {
        return BaseEnum.toEnumConstant(MethodExchange.class, code);
    }
}

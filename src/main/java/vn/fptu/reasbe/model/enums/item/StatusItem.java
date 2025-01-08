package vn.fptu.reasbe.model.enums.item;

import vn.fptu.reasbe.model.enums.core.BaseEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author ntig
 */
@Getter
@RequiredArgsConstructor
public enum StatusItem implements BaseEnum.CodeAccessible {
    AVAILABLE("AVAI"),
    SOLD_OUT("SOLD"),
    EXPIRED("EXPI"), // expired because owner not extend display time of item on app
    NO_LONGER_FOR_SALE("NLFS"); // owner does not want to sale that item any more

    private final String code;

    public static StatusItem toEnumConstant(String code) {
        return BaseEnum.toEnumConstant(StatusItem.class, code);
    }
}

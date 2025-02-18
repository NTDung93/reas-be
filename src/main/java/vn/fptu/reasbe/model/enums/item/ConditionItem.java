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
public enum ConditionItem implements BaseEnum.CodeAccessible {
    BRAND_NEW("BNEW"),
    LIKE_NEW("LNEW"),
    EXCELLENT("EXCE"),
    GOOD("GOOD"),
    FAIR("FAIR"),
    POOR("POOR"),
    NOT_WORKING("NOWO");

    private final String code;

    public static ConditionItem toEnumConstant(String code) {
        return BaseEnum.toEnumConstant(ConditionItem.class, code);
    }
}

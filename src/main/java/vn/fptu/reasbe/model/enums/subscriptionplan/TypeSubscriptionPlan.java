package vn.fptu.reasbe.model.enums.subscriptionplan;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.enums.core.BaseEnum;

/**
 *
 * @author dungnguyen
 */
@Getter
@RequiredArgsConstructor
public enum TypeSubscriptionPlan implements BaseEnum.CodeAccessible {
    PREMIUM_PLAN("PREM"),
    ITEM_EXTENSION("IEXT");

    private final String code;

    public static TypeSubscriptionPlan toEnumConstant(String code) {
        return BaseEnum.toEnumConstant(TypeSubscriptionPlan.class, code);
    }
}

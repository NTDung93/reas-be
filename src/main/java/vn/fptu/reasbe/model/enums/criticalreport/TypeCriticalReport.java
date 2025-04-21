package vn.fptu.reasbe.model.enums.criticalreport;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.enums.core.BaseEnum;

@Getter
@RequiredArgsConstructor
public enum TypeCriticalReport implements BaseEnum.CodeAccessible{
    USER("USER"),
    FEEDBACK("FEEDBACK"),
    EXCHANGE("EXCHANGE");

    private final String code;

    public static TypeCriticalReport toEnumConstant(String code) {
        return BaseEnum.toEnumConstant(TypeCriticalReport.class, code);
    }
}

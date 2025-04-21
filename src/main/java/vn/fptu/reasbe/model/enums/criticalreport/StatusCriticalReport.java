package vn.fptu.reasbe.model.enums.criticalreport;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.enums.core.BaseEnum;

@Getter
@RequiredArgsConstructor
public enum StatusCriticalReport implements BaseEnum.CodeAccessible{
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private final String code;

    public static StatusCriticalReport toEnumConstant(String code) {
        return BaseEnum.toEnumConstant(StatusCriticalReport.class, code);
    }
}

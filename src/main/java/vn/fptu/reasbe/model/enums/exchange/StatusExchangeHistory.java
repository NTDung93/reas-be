package vn.fptu.reasbe.model.enums.exchange;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.enums.core.BaseEnum;
import vn.fptu.reasbe.model.enums.payment.MethodPayment;

@Getter
@RequiredArgsConstructor
public enum StatusExchangeHistory implements BaseEnum.CodeAccessible {
    NOT_YET_EXCHANGE("NYEX"),
    PENDING_EVIDENCE("PDEV"),
    SUCCESSFUL("SUCC"),
    FAILED("FAIL");

    private final String code;

    public static MethodPayment toEnumConstant(String code) {
        return BaseEnum.toEnumConstant(MethodPayment.class, code);
    }
}

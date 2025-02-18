package vn.fptu.reasbe.model.enums.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.enums.core.BaseEnum;

/**
 *
 * @author ntig
 */
@Getter
@RequiredArgsConstructor
public enum MethodPayment implements BaseEnum.CodeAccessible {
    CASH("CASH"),
    BANK_TRANSFER("BANK"),
    CREDIT_CARD("CREDIT"),
    PAYPAL("PAYPAL"),
    APPLE_PAY("APPLE"),
    VISA("VISA"),
    MASTER_CARD("MASTER"),
    OTHER("OTHER");

    private final String code;

    public static MethodPayment toEnumConstant(String code) {
        return BaseEnum.toEnumConstant(MethodPayment.class, code);
    }
}

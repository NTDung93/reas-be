package vn.fptu.reasbe.model.enums.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.enums.core.BaseEnum;

/**
 *
 * @author ntig
 */
@Getter
@RequiredArgsConstructor
public enum TypeNotification implements BaseEnum.CodeAccessible{
    TRANSACTION_SUCCESS("TR_SUCCESS"),
    TRANSACTION_FAILED("TR_FAILED"),
    EXCHANGE_REQUEST("EX_REQ"),
    BUY_REQUEST("BUY_REQ"),
    SYSTEM_NOTIFICATION("SYS_NOTI");

    private final String code;

    public static TypeNotification toEnumConstant(String code){
        return BaseEnum.toEnumConstant(TypeNotification.class, code);
    }
}

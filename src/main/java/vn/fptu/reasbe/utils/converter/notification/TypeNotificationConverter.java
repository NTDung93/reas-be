package vn.fptu.reasbe.utils.converter.notification;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.model.enums.notification.TypeNotification;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

/**
 *
 * @author ntig
 */
@Converter(autoApply = true)
public class TypeNotificationConverter extends BaseEnumConverter<TypeNotification> {

    public TypeNotificationConverter(){
        super(TypeNotification.class);
    }
}

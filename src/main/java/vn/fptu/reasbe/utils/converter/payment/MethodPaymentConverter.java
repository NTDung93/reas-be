package vn.fptu.reasbe.utils.converter.payment;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.model.enums.payment.MethodPayment;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

/**
 *
 * @author ntig
 */
@Converter(autoApply = true)
public class MethodPaymentConverter extends BaseEnumConverter<MethodPayment> {

    public MethodPaymentConverter() {
        super(MethodPayment.class);
    }
}

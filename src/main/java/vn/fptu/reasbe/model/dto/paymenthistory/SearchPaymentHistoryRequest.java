package vn.fptu.reasbe.model.dto.paymenthistory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.payment.MethodPayment;
import vn.fptu.reasbe.model.enums.payment.StatusPayment;

/**
 *
 * @author dungnguyen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchPaymentHistoryRequest {
    Integer userId;
    Long transactionId;
    BigDecimal price;
    BigDecimal fromPrice;
    BigDecimal toPrice;
    List<StatusPayment> statusPayments;
    List<MethodPayment> methodPayments;
    List<StatusEntity> statusEntities;
    LocalDateTime fromTransactionDate;
    LocalDateTime toTransactionDate;
}

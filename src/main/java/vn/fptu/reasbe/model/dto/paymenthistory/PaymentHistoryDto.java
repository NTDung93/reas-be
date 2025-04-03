package vn.fptu.reasbe.model.dto.paymenthistory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.payment.MethodPayment;
import vn.fptu.reasbe.model.enums.payment.StatusPayment;
import vn.fptu.reasbe.model.enums.subscriptionplan.TypeSubscriptionPlan;

/**
 *
 * @author dungnguyen
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentHistoryDto {
    Integer id;
    Long transactionId;
    BigDecimal amount;
    String description; // noi dung chuyen khoan
    LocalDateTime transactionDateTime;
    StatusPayment statusPayment;
    MethodPayment methodPayment;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String planName; // plan name
    TypeSubscriptionPlan typeSubscriptionPlan;
    Float duration; // count by month
}

package vn.fptu.reasbe.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;
import vn.fptu.reasbe.model.enums.payment.MethodPayment;
import vn.fptu.reasbe.model.enums.payment.StatusPayment;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PAYMENT_HISTORY")
@AttributeOverride(name = "id", column = @Column(name = "PAYMENT_HISTORY_ID"))
public class PaymentHistory extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;

    @NotNull
    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description; // noi dung chuyen khoan

    @NotNull
    @Column(name = "TRANSACTION_DATE_TIME")
    private LocalDateTime transactionDateTime;

    @NotNull
    @Column(name = "STATUS_PAYMENT")
    @Enumerated(EnumType.STRING)
    private StatusPayment statusPayment;

    @Column(name = "METHOD_PAYMENT")
    private MethodPayment methodPayment;

    @OneToOne(mappedBy = "paymentHistory")
    private UserSubscription userSubscription;
}

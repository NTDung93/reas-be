package vn.fptu.reasbe.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;

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
@Table(name = "USER_SUBSCRIPTION")
@AttributeOverride(name = "id", column = @Column(name = "USER_SUBSCRIPTION_ID"))
public class UserSubscription extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @NotNull
    @Column(name = "NUMBER_OF_FREE_EXTENSION_LEFT")
    private Integer numberOfFreeExtensionLeft;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_PLAN_ID")
    private SubscriptionPlan subscriptionPlan;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PAYMENT_HISTORY_ID")
    private PaymentHistory paymentHistory;
}

package vn.fptu.reasbe.model.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;
import vn.fptu.reasbe.model.enums.exchange.StatusExchange;

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
@Table(name = "EXCHANGE_HISTORY")
@AttributeOverride(name = "id", column = @Column(name = "EXCHANGE_HISTORY_ID"))
public class ExchangeHistory extends AbstractAuditableEntity {

    @ManyToOne
    @JoinColumn(name = "SELLER_ID")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "BUYER_ID")
    private User buyer;

    @Column(name = "BUYER_CONFIRMATION")
    private Boolean buyerConfirmation;

    @Column(name = "SELLER_CONFIRMATION")
    private Boolean sellerConfirmation;

    @NotNull
    @Column(name = "STATUS_EXCHANGE")
    @Enumerated(EnumType.STRING)
    private StatusExchange statusExchange;

    @OneToMany(mappedBy = "exchangeHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PaymentHistory> paymentHistories = new HashSet<>();

    @OneToMany(mappedBy = "exchangeHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExchangeDetail> exchangeDetails = new HashSet<>();
}

package vn.fptu.reasbe.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeRequest;
import vn.fptu.reasbe.model.enums.item.MethodExchange;

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
@Table(name = "EXCHANGE_REQUEST")
@AttributeOverride(name = "id", column = @Column(name = "EXCHANGE_REQUEST_ID"))
public class ExchangeRequest extends AbstractAuditableEntity {

    @ManyToOne
    @JoinColumn(name = "SELLER_ITEM_ID")
    private Item sellerItem;

    @ManyToOne
    @JoinColumn(name = "BUYER_ITEM_ID")
    private Item buyerItem;

    @OneToOne
    @JoinColumn(name = "PAID_BY_ID")
    private User paidBy;

    @Column(name = "EXCHANGE_DATE")
    private LocalDateTime exchangeDate;

    @Column(name = "EXCHANGE_LOCATION")
    private String exchangeLocation;

    @NotNull
    @Column(name = "ESTIMATE_PRICE")
    private BigDecimal estimatePrice;

    @NotNull
    @Column(name = "FINAL_PRICE")
    private BigDecimal finalPrice;

    @NotNull
    @Column(name = "METHOD_EXCHANGE")
    private MethodExchange methodExchange;

    @NotNull
    @Column(name = "STATUS_REQUEST")
    @Enumerated(EnumType.STRING)
    private StatusExchangeRequest statusExchangeRequest;

    @Column(name = "BUYER_CONFIRMATION")
    private Boolean buyerConfirmation;

    @Column(name = "SELLER_CONFIRMATION")
    private Boolean sellerConfirmation;

    @Column(name = "ADDITIONAL_NOTES")
    private String additionalNotes;

    @OneToOne
    @JoinColumn(name = "EXCHANGE_HISTORY_ID")
    private ExchangeHistory exchangeHistory;
}

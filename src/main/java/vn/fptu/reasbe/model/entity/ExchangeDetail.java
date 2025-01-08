package vn.fptu.reasbe.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "EXCHANGE_DETAIL")
@AttributeOverride(name = "id", column = @Column(name = "EXCHANGE_DETAIL_ID"))
public class ExchangeDetail extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;

    @NotNull
    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;

    @Column(name = "ADDITIONAL_NOTE")
    private String additionalNote;

    @NotNull
    @Column(name = "ITEM_CONDITION")
    private String itemCondition; // TODO: ntig change to enum ItemCondition later

    @ManyToOne
    @JoinColumn(name = "EXCHANGE_HISTORY_ID")
    private ExchangeHistory exchangeHistory;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
}

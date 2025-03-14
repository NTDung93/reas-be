package vn.fptu.reasbe.model.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;

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
    @NotNull
    @Column(name = "BUYER_CONFIRMATION")
    private Boolean buyerConfirmation;

    @NotNull
    @Column(name = "SELLER_CONFIRMATION")
    private Boolean sellerConfirmation;

    @Column(name = "BUYER_ITEM_IMAGE_URL")
    private String buyerItemImageUrl;

    @Column(name = "SELLER_ITEM_IMAGE_URL")
    private String sellerItemImageUrl;

    @Column(name = "BUYER_TRANSACTION_IMAGE_URL")
    private String buyerTransactionImageUrl;

    @Column(name = "SELLER_TRANSACTION_IMAGE_URL")
    private String sellerTransactionImageUrl;

    @Column(name = "BUYER_ADDITIONAL_NOTES")
    private String buyerAdditionalNotes;

    @Column(name = "SELLER_ADDITIONAL_NOTES")
    private String sellerAdditionalNotes;

    @NotNull
    @Column(name = "STATUS_EXCHANGE_HISTORY", length = 4)
    private StatusExchangeHistory statusExchangeHistory;

    @OneToMany(mappedBy = "exchangeHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToOne(mappedBy = "exchangeHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExchangeRequest exchangeRequest;
}

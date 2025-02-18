package vn.fptu.reasbe.model.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Table(name = "EXCHANGE_HISTORY")
@AttributeOverride(name = "id", column = @Column(name = "EXCHANGE_HISTORY_ID"))
public class ExchangeHistory extends AbstractAuditableEntity {
    @NotNull
    @Column(name = "BUYER_CONFIRMATION")
    private Boolean buyerConfirmation;

    @NotNull
    @Column(name = "SELLER_CONFIRMATION")
    private Boolean sellerConfirmation;

    @Column(name = "ADDITIONAL_NOTES")
    private String additionalNotes;

    @OneToMany(mappedBy = "exchangeHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Feedback> feedbacks = new HashSet<>();
}

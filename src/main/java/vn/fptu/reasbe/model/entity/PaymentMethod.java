package vn.fptu.reasbe.model.entity;

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
@Table(name = "PAYMENT_METHOD")
@AttributeOverride(name = "id", column = @Column(name = "PAYMENT_METHOD_ID"))
public class PaymentMethod extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "TYPE_METHOD")
    private String typeMethod; // TODO: ntig change to enum TypeMethod later

    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PaymentHistory> paymentHistories;
}

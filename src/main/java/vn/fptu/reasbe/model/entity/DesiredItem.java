package vn.fptu.reasbe.model.entity;

import java.math.BigDecimal;
import jakarta.persistence.AttributeOverride;
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
import org.hibernate.Length;
import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;
import vn.fptu.reasbe.model.enums.item.ConditionItem;

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
@Table(name = "DESIRED_ITEM")
@AttributeOverride(name = "id", column = @Column(name = "DESIRED_ITEM_ID"))
public class DesiredItem extends AbstractAuditableEntity {

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Column(name = "CONDITION_ITEM", length = 4)
    private ConditionItem conditionItem;

    @ManyToOne
    @JoinColumn(name = "BRAND_ID")
    private Brand brand;

    @NotNull
    @Column(name = "MIN_PRICE")
    private BigDecimal minPrice;

    @Column(name = "MAX_PRICE")
    private BigDecimal maxPrice;

    @NotNull
    @Column(name = "DESCRIPTION", length = Length.LOB_DEFAULT)
    private String description;

    @OneToOne(mappedBy = "desiredItem")
    private Item item;
}
package vn.fptu.reasbe.model.entity;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Length;

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
import vn.fptu.reasbe.model.enums.item.ConditionItem;
import vn.fptu.reasbe.model.enums.item.MethodExchange;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.model.enums.item.TypeExchange;

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
@Table(name = "ITEM")
@AttributeOverride(name = "id", column = @Column(name = "ITEM_ID"))
public class Item extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "ITEM_NAME", length = 50)
    private String itemName;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "PRICE")
    private BigDecimal price;

    @NotNull
    @Column(name = "IMAGE_URL", length = Length.LOB_DEFAULT)
    private String imageUrl;

    @NotNull
    @Column(name = "CONDITION_ITEM", length = 4)
    private ConditionItem conditionItem;

    @NotNull
    @Column(name = "STATUS_ITEM", length = 4)
    private StatusItem statusItem;

    @NotNull
    @Column(name = "METHOD_EXCHANGE")
    private List<MethodExchange> methodExchanges;

    @NotNull
    @Column(name = "TYPE_EXCHANGE", length = 4)
    private TypeExchange typeExchange;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "BRAND_ID")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "USER_LOCATION_ID")
    private UserLocation userLocation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DESIRED_ITEM_ID")
    private DesiredItem desiredItem;
}

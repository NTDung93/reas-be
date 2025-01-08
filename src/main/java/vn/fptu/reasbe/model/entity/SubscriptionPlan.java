package vn.fptu.reasbe.model.entity;

import java.math.BigDecimal;
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
@Table(name = "SUBSCRIPTION_PLAN")
@AttributeOverride(name = "id", column = @Column(name = "SUBSCRIPTION_PLAN_ID"))
public class SubscriptionPlan extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "PRICE")
    private BigDecimal price;

    @NotNull
    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @NotNull
    @Column(name = "TYPE_SUBSCRIPTION_PLAN")
    private String typeSubscriptionPlan; // TODO: ntig change to enum TypeSubscriptionPlan later

    @NotNull
    @Column(name = "DURATION")
    private int duration; // count by month

    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSubscription> userSubscriptions = new HashSet<>();
}

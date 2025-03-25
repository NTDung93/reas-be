package vn.fptu.reasbe.model.dto.subscriptionplan;

import java.math.BigDecimal;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.subscriptionplan.TypeSubscriptionPlan;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchSubscriptionPlanRequest {
    String name;
    String description;
    BigDecimal price;
    BigDecimal fromPrice;
    BigDecimal toPrice;
    List<TypeSubscriptionPlan> typeSubscriptionPlans;
    Float duration;
    Float fromDuration;
    Float toDuration;
    List<StatusEntity> statusEntities;
}

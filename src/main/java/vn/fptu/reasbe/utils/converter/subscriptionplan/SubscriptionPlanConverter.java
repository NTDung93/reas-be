package vn.fptu.reasbe.utils.converter.subscriptionplan;

import jakarta.persistence.Converter;
import vn.fptu.reasbe.model.enums.subscriptionplan.TypeSubscriptionPlan;
import vn.fptu.reasbe.utils.converter.core.BaseEnumConverter;

/**
 *
 * @author dungnguyen
 */
@Converter(autoApply = true)
public class SubscriptionPlanConverter extends BaseEnumConverter<TypeSubscriptionPlan> {

    public SubscriptionPlanConverter() {
        super(TypeSubscriptionPlan.class);
    }
}

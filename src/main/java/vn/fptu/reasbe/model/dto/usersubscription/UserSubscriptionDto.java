package vn.fptu.reasbe.model.dto.usersubscription;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.dto.subscriptionplan.SubscriptionPlanDto;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSubscriptionDto {
    LocalDateTime startDate;
    LocalDateTime endDate;
    SubscriptionPlanDto subscriptionPlan;
    Integer numberOfFreeExtensionLeft;
}

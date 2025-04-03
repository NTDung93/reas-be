package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.entity.UserSubscription;
import vn.fptu.reasbe.model.enums.subscriptionplan.TypeSubscriptionPlan;

import java.time.LocalDateTime;

/**
 *
 * @author dungnguyen
 */
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    boolean existsByUserAndSubscriptionPlan_TypeSubscriptionPlanAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            User user,
            TypeSubscriptionPlan typeSubscriptionPlan,
            LocalDateTime currentDate1,
            LocalDateTime currentDate2
    );
}

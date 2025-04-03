package vn.fptu.reasbe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import jakarta.validation.constraints.NotNull;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.subscriptionplan.TypeSubscriptionPlan;
import vn.fptu.reasbe.repository.custom.SubscriptionPlanRepositoryCustom;

/**
 *
 * @author dungnguyen
 */
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer>, QuerydslPredicateExecutor<SubscriptionPlan>, SubscriptionPlanRepositoryCustom {
    boolean existsByNameContainsIgnoreCaseAndStatusEntityEquals(String name, StatusEntity statusEntity);

    Optional<SubscriptionPlan> findSubscriptionPlanById(Integer id);

    boolean existsByNameContainsIgnoreCaseAndStatusEntityEqualsAndIdIsNot(String name, StatusEntity statusEntity, Integer id);

    Optional<SubscriptionPlan> findSubscriptionPlanByTypeSubscriptionPlanAndStatusEntity(@NotNull TypeSubscriptionPlan typeSubscriptionPlan, @NotNull StatusEntity statusEntity);
}

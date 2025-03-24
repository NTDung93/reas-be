package vn.fptu.reasbe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.fptu.reasbe.model.entity.SubscriptionPlan;
import vn.fptu.reasbe.repository.custom.SubscriptionPlanRepositoryCustom;

/**
 *
 * @author dungnguyen
 */
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer>, QuerydslPredicateExecutor<SubscriptionPlan>, SubscriptionPlanRepositoryCustom {
    boolean existsByNameContainsIgnoreCase(String name);
    Optional<SubscriptionPlan> findSubscriptionPlanById (Integer id);
}

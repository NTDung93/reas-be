package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.fptu.reasbe.model.entity.UserSubscription;

/**
 *
 * @author dungnguyen
 */
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
}

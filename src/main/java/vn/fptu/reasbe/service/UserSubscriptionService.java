package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.model.entity.UserSubscription;

/**
 *
 * @author dungnguyen
 */
public interface UserSubscriptionService {
    UserSubscription createUserSubscription(Integer subscriptionPlanId, PaymentHistory paymentHistory);
}

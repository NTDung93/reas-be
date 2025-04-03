package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.usersubscription.UserSubscriptionDto;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;

/**
 *
 * @author dungnguyen
 */
public interface UserSubscriptionService {
    void createUserSubscription(SubscriptionPlan plan, Item item, PaymentHistory paymentHistory);

    UserSubscriptionDto getUserCurrentSubscription();
}

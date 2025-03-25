package vn.fptu.reasbe.service.impl;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.entity.UserSubscription;
import vn.fptu.reasbe.repository.UserSubscriptionRepository;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.SubscriptionPlanService;
import vn.fptu.reasbe.service.UserSubscriptionService;

/**
 *
 * @author dungnguyen
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionPlanService subscriptionPlanService;
    private final AuthService authService;

    @Override
    public UserSubscription createUserSubscription(Integer subscriptionPlanId, PaymentHistory paymentHistory) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanService.getSubscriptionPlanByPlanId(subscriptionPlanId);
        User currentUser = authService.getCurrentUser();
        Float duration = subscriptionPlan.getDuration();

        UserSubscription userSubscription = UserSubscription.builder()
                .user(currentUser)
                .subscriptionPlan(subscriptionPlan)
                .paymentHistory(paymentHistory)
                .startDate(paymentHistory.getTransactionDateTime())
                .endDate(paymentHistory.getTransactionDateTime().plusSeconds((long) (duration * 24 * 60 * 60)))
                .build();

        return userSubscriptionRepository.save(userSubscription);
    }
}

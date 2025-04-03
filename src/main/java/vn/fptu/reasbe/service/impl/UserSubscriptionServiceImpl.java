package vn.fptu.reasbe.service.impl;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.usersubscription.UserSubscriptionDto;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.entity.UserSubscription;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.payment.StatusPayment;
import vn.fptu.reasbe.model.enums.subscriptionplan.TypeSubscriptionPlan;
import vn.fptu.reasbe.repository.UserSubscriptionRepository;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.UserSubscriptionService;
import vn.fptu.reasbe.utils.common.DateUtils;
import vn.fptu.reasbe.utils.mapper.UserSubscriptionMapper;

/**
 *
 * @author dungnguyen
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final AuthService authService;
    private final UserSubscriptionMapper userSubscriptionMapper;

    @Override
    public void createUserSubscription(SubscriptionPlan subscriptionPlan, Item item, PaymentHistory paymentHistory) {
        User currentUser = authService.getCurrentUser();
        Float duration = subscriptionPlan.getDuration();

        UserSubscription userSubscription = UserSubscription.builder()
                .user(currentUser)
                .subscriptionPlan(subscriptionPlan)
                .item(item)
                .paymentHistory(paymentHistory)
                .startDate(paymentHistory.getTransactionDateTime())
                .endDate(paymentHistory.getTransactionDateTime().plusSeconds((long) (duration * 24 * 60 * 60)))
                .build();

        userSubscriptionRepository.save(userSubscription);
    }

    @Override
    public UserSubscriptionDto getUserCurrentSubscription() {
        User currentUser = authService.getCurrentUser();
        UserSubscription currentUserSubscription = userSubscriptionRepository.findByUserIdAndSubscriptionPlan_TypeSubscriptionPlanAndEndDateIsAfterAndPaymentHistory_StatusPaymentAndStatusEntity(
                currentUser.getId(),
                TypeSubscriptionPlan.PREMIUM_PLAN,
                DateUtils.getCurrentDateTime(),
                StatusPayment.SUCCESS,
                StatusEntity.ACTIVE
        );
        if (currentUserSubscription != null) {
            return userSubscriptionMapper.toDto(currentUserSubscription);
        } else {
            return null;
        }
    }
}

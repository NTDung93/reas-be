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

import java.time.LocalDateTime;
import java.time.YearMonth;

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
    public void createUserSubscription(SubscriptionPlan subscriptionPlan, User user, Item item, PaymentHistory paymentHistory) {
        Float duration = subscriptionPlan.getDuration();

        LocalDateTime startDate = paymentHistory.getTransactionDateTime();

        LocalDateTime endDate = calculateSubscriptionEndDate(startDate, duration);

        UserSubscription userSubscription = UserSubscription.builder()
                .user(user)
                .subscriptionPlan(subscriptionPlan)
                .item(item)
                .paymentHistory(paymentHistory)
                .startDate(startDate)
                .endDate(endDate)
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

    public static LocalDateTime calculateSubscriptionEndDate(LocalDateTime startDate, float duration) {
        long fullMonths = (long) duration;
        double fractionalMonth = duration - fullMonths;

        LocalDateTime intermediateDate = startDate.plusMonths(fullMonths);

        YearMonth yearMonth = YearMonth.from(intermediateDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        long extraDays = Math.round(fractionalMonth * daysInMonth);

        return intermediateDate.plusDays(extraDays);
    }
}

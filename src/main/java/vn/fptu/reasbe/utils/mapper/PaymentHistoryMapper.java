package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import vn.fptu.reasbe.model.dto.paymenthistory.PaymentHistoryDto;
import vn.fptu.reasbe.model.entity.PaymentHistory;

/**
 *
 * @author dungnguyen
 */
@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
@Component
public interface PaymentHistoryMapper {
    @Mapping(target = "startDate", source = "userSubscription.startDate")
    @Mapping(target = "endDate", source = "userSubscription.endDate")
    @Mapping(target = "planName", source = "userSubscription.subscriptionPlan.name")
    @Mapping(target = "typeSubscriptionPlan", source = "userSubscription.subscriptionPlan.typeSubscriptionPlan")
    @Mapping(target = "duration", source = "userSubscription.subscriptionPlan.duration")
    PaymentHistoryDto toDto(PaymentHistory entity);
}

package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import vn.fptu.reasbe.model.dto.subscriptionplan.SubscriptionPlanDto;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;

/**
 *
 * @author dungnguyen
 */
@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
@Component
public interface SubscriptionPlanMapper {
    SubscriptionPlanDto toDto(SubscriptionPlan entity);

    SubscriptionPlan toEntity(SubscriptionPlanDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget SubscriptionPlan entity, SubscriptionPlanDto dto);
}

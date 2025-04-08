package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import vn.fptu.reasbe.model.dto.mongodb.notification.NotificationDto;
import vn.fptu.reasbe.model.mongodb.Notification;

/**
 *
 * @author dungnguyen
 */
@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
@Component
public interface NotificationMapper {
    NotificationDto toDto(Notification entity);
}

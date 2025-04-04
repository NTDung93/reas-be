package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationDto;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationRequest;
import vn.fptu.reasbe.model.entity.UserLocation;

/**
 *
 * @author ntig
 */
@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                LocationMapper.class
        }
)
@Component
public interface UserLocationMapper {
    @Mapping(target = "userId", source = "user.id")
    UserLocationDto toDto(UserLocation userLocation);

    UserLocation toEntity(UserLocationRequest request);

    void updateEntity(@MappingTarget UserLocation entity, UserLocationRequest request);
}

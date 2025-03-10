package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationDto;
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
    UserLocationMapper INSTANCE = Mappers.getMapper(UserLocationMapper.class);

    @Mapping(target = "userId", source = "user.id")
    UserLocationDto toDto(UserLocation userLocation);
}

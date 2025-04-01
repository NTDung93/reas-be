package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.location.LocationDto;
import vn.fptu.reasbe.model.entity.Location;

/**
 *
 * @author ntig
 */
@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
@Component
public interface LocationMapper {
    LocationDto toDto(Location location);
}

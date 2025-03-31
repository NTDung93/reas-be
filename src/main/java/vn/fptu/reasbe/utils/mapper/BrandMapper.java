package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import vn.fptu.reasbe.model.dto.brand.BrandDto;
import vn.fptu.reasbe.model.dto.brand.BrandResponse;
import vn.fptu.reasbe.model.entity.Brand;

/**
 *
 * @author ntig
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    BrandDto toDto(Brand brand);

    BrandResponse toResponse(Brand brand);
}

package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import vn.fptu.reasbe.model.dto.category.CategoryDto;
import vn.fptu.reasbe.model.dto.category.CategoryResponse;
import vn.fptu.reasbe.model.entity.Category;

/**
 *
 * @author ntig
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);

    CategoryResponse toResponse(Category category);
}

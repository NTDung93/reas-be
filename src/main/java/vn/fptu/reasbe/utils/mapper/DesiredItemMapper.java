package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.desireditem.DesiredItemDto;
import vn.fptu.reasbe.model.dto.desireditem.DesiredItemResponse;
import vn.fptu.reasbe.model.entity.DesiredItem;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                BrandMapper.class,
                CategoryMapper.class
        }

)
@Component
public interface DesiredItemMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "item", ignore = true)
    DesiredItem toDesiredItem(DesiredItemDto dto);

    @Mapping(target = "typeItem", source = "category.typeItem")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(target = "brandName", source = "brand.brandName")
    DesiredItemResponse toDesiredItemResponse(DesiredItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "item", ignore = true)
    void updateDesiredItem(@MappingTarget DesiredItem desiredItem, DesiredItemDto dto);
}

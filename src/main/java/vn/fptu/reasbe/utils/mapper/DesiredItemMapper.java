package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.desireditem.DesiredItemDto;
import vn.fptu.reasbe.model.dto.desireditem.DesiredItemResponse;
import vn.fptu.reasbe.model.entity.DesiredItem;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
@Component
public interface DesiredItemMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "item", ignore = true)
    DesiredItem toDesiredItem(DesiredItemDto dto);

    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(target = "brandName", source = "brand.brandName")
    DesiredItemResponse toDesiredItemResponse(DesiredItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "statusEntity", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModificationDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateDesiredItem(@MappingTarget DesiredItem desiredItem, DesiredItemDto dto);
}

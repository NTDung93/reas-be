package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.item.ItemResponse;
import vn.fptu.reasbe.model.dto.item.UpdateItemRequest;
import vn.fptu.reasbe.model.dto.item.UploadItemRequest;
import vn.fptu.reasbe.model.dto.item.SearchItemResponse;
import vn.fptu.reasbe.model.entity.Item;

import java.util.List;

/**
 *
 * @author ntig
 */
@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                CategoryMapper.class,
                BrandMapper.class,
                LocationMapper.class,
                UserLocationMapper.class,
                UserMapper.class,
                DesiredItemMapper.class
        }
)
@Component
public interface ItemMapper {
    @Mapping(target = "favorite", expression = "java(checkIfFavorite(item.getId(), favIds))")
    SearchItemResponse toSearchItemResponse(Item item, List<Integer> favIds);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "desiredItem", ignore = true)
    @Mapping(target = "statusItem", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "userLocation", ignore = true)
    @Mapping(target = "expiredTime", ignore = true)
    Item toEntity(UploadItemRequest dto);

    @Mapping(target = "typeItem", source = "category.typeItem")
    ItemResponse toItemResponse(Item item);

    @Mapping(target = "typeItem", source = "item.category.typeItem")
    @Mapping(target = "favorite", expression = "java(checkIfFavorite(item.getId(), favIds))")
    ItemResponse toItemResponseWithFavorite(Item item, List<Integer> favIds);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "desiredItem", ignore = true)
    @Mapping(target = "statusItem", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "userLocation", ignore = true)
    @Mapping(target = "expiredTime", ignore = true)
    @Mapping(target = "moneyAccepted", source = "isMoneyAccepted")
    void updateItem(@MappingTarget Item user, UpdateItemRequest request);

    default boolean checkIfFavorite(Integer itemId, List<Integer> favIds) {
        return favIds != null && !favIds.isEmpty() && favIds.contains(itemId);
    }
}

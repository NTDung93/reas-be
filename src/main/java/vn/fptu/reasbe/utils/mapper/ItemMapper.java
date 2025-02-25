package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.*;

import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.item.ItemResponse;
import vn.fptu.reasbe.model.dto.item.UpdateItemRequest;
import vn.fptu.reasbe.model.dto.item.UploadItemRequest;
import vn.fptu.reasbe.model.dto.item.SearchItemResponse;
import vn.fptu.reasbe.model.entity.Item;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    SearchItemResponse toSearchItemResponse(Item person);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "desiredItem", ignore = true)
    @Mapping(target = "statusItem", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "userLocation", ignore = true)
    @Mapping(target = "expiredTime", ignore = true)
    Item toEntity(UploadItemRequest dto);

    ItemResponse toItemResponse(Item item);

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
}

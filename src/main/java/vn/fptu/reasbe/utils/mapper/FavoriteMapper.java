package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.favorite.FavoriteResponse;
import vn.fptu.reasbe.model.entity.Favorite;

import java.util.List;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                ItemMapper.class,
                UserMapper.class
        }
)
@Component
public interface FavoriteMapper {
    @Mapping(target = "item", expression = "java(itemMapper.toItemResponseWithFavorite(favorite.getItem(), favIds))")
    FavoriteResponse toResponseWithFavorite(Favorite favorite, List<Integer> favIds, @Context ItemMapper itemMapper);
}

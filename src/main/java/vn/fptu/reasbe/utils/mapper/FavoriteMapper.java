package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import vn.fptu.reasbe.model.dto.favorite.FavoriteResponse;
import vn.fptu.reasbe.model.entity.Favorite;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                ItemMapper.class,
                UserMapper.class
        }
)
public interface FavoriteMapper {
    FavoriteResponse toResponse(Favorite favorite);
}

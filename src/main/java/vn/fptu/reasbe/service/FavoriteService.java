package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.favorite.FavoriteResponse;

public interface FavoriteService {
    BaseSearchPaginationResponse<FavoriteResponse> getAllFavoriteItems(int pageNo, int pageSize, String sortBy, String sortDir);

    FavoriteResponse addToFavorite(Integer itemId);

    Boolean deleteFromFavorite(Integer favoriteId);
}

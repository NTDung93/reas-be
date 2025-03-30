package vn.fptu.reasbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.favorite.FavoriteResponse;
import vn.fptu.reasbe.model.entity.Favorite;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.repository.FavoriteRepository;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.FavoriteService;
import vn.fptu.reasbe.service.ItemService;
import vn.fptu.reasbe.utils.mapper.FavoriteMapper;

import static vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse.getPageable;

@Service

@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final AuthService authService;
    private final ItemService itemService;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;

    @Override
    public BaseSearchPaginationResponse<FavoriteResponse> getAllFavoriteItems(int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = authService.getCurrentUser();

        return BaseSearchPaginationResponse.of(favoriteRepository.findAllByUser(user, getPageable(pageNo, pageSize, sortBy, sortDir)).map(favoriteMapper::toResponse));
    }

    @Override
    public FavoriteResponse addToFavorite(Integer itemId) {
        User user = authService.getCurrentUser();

        Item item = itemService.getItemById(itemId);

        if (!item.getStatusItem().equals(StatusItem.AVAILABLE)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.cannotAddToFavorite");
        }

        Favorite favoriteItem = Favorite.builder()
                .item(item)
                .user(user)
                .build();

        return favoriteMapper.toResponse(favoriteRepository.save(favoriteItem));
    }

    @Override
    public Boolean deleteFromFavorite(Integer favoriteId) {
        User user = authService.getCurrentUser();

        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ReasApiException(HttpStatus.BAD_REQUEST, "error.cannotDeleteFromFavorite"));

        if (!favorite.getUser().equals(user)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.invalidUser");
        }

        favoriteRepository.delete(favorite);

        return true ;
    }
}

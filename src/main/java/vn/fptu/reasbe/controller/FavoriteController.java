package vn.fptu.reasbe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.favorite.FavoriteResponse;
import vn.fptu.reasbe.service.FavoriteService;

@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    @GetMapping
    public ResponseEntity<BaseSearchPaginationResponse<FavoriteResponse>> getAllFavoriteItems(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(favoriteService.getAllFavoriteItems(pageNo, pageSize, sortBy, sortDir));
    }

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    @PostMapping
    public ResponseEntity<FavoriteResponse> addToFavorite(@RequestParam Integer itemId) {
        return ResponseEntity.ok(favoriteService.addToFavorite(itemId));
    }

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    @DeleteMapping
    public ResponseEntity<Boolean> deleteFromFavorite(@RequestParam Integer id) {
        return ResponseEntity.ok(favoriteService.deleteFromFavorite(id));
    }
}

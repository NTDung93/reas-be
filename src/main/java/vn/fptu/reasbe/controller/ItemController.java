package vn.fptu.reasbe.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.item.*;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.service.ItemService;
import vn.fptu.reasbe.utils.mapper.ItemMapper;

/**
 * @author ntig
 */
@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping("/search")
    public ResponseEntity<BaseSearchPaginationResponse<SearchItemResponse>> searchItemPagination(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestBody @Nullable SearchItemRequest request
    ) {
        return ResponseEntity.ok(itemService.searchItemPagination(pageNo, pageSize, sortBy, sortDir, request));
    }

    @GetMapping("/user")
    public ResponseEntity<BaseSearchPaginationResponse<ItemResponse>> getAllAvailableItemOfUser(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "statusItem") StatusItem statusItem)
    {
        return ResponseEntity.ok(itemService.getAllItemOfUser(pageNo, pageSize, sortBy, sortDir, userId, statusItem));
    }

    @GetMapping("/current-user")
    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    public ResponseEntity<BaseSearchPaginationResponse<ItemResponse>> getAllItemOfCurrentUserByStatus(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
             @RequestParam(value = "statusItem") StatusItem statusItem)
    {
        return ResponseEntity.ok(itemService.getAllItemOfCurrentUserByStatusItem(pageNo, pageSize, sortBy, sortDir, statusItem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemDetail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(itemMapper.toItemResponse(itemService.getItemById(id)));
    }

    @SecurityRequirement(name = AppConstants.SEC_REQ_NAME)
    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    @PostMapping()
    public ResponseEntity<ItemResponse> uploadItem(@Valid @RequestBody UploadItemRequest request) {
        return ResponseEntity.ok(itemMapper.toItemResponse(itemService.uploadItem(request)));
    }

    @SecurityRequirement(name = AppConstants.SEC_REQ_NAME)
    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    @PutMapping()
    public ResponseEntity<ItemResponse> updateItem(@Valid @RequestBody UpdateItemRequest request) {
        return ResponseEntity.ok(itemService.updateItem(request));
    }

    @SecurityRequirement(name = AppConstants.SEC_REQ_NAME)
    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_STAFF)")
    @GetMapping("/pending")
    public ResponseEntity<BaseSearchPaginationResponse<ItemResponse>> getAllPendingItem(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(itemService.getAllPendingItem(pageNo, pageSize, sortBy, sortDir));
    }

    @SecurityRequirement(name = AppConstants.SEC_REQ_NAME)
    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_STAFF)")
    @PutMapping("/review")
    public ResponseEntity<ItemResponse> reviewItem(@RequestParam("id") Integer id, @RequestParam("statusItem") StatusItem statusItem) {
        return ResponseEntity.ok(itemService.reviewItem(id, statusItem));
    }
}

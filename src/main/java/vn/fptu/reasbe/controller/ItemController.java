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

import java.util.List;

/**
 * @author ntig
 */
@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/search")
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
    public ResponseEntity<List<ItemResponse>> getAllAvailableItemOfUser(
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "statusItem") StatusItem statusItem)
    {
        return ResponseEntity.ok(itemService.getAllItemOfUser(userId, statusItem));
    }

    @GetMapping("/current-user")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<List<ItemResponse>> getAllItemOfCurrentUserByStatus(
             @RequestParam(value = "statusItem") StatusItem statusItem)
    {
        return ResponseEntity.ok(itemService.getAllItemOfCurrentUserByStatusItem(statusItem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemDetail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(itemService.getItemDetail(id));
    }

    @SecurityRequirement(name = AppConstants.SEC_REQ_NAME)
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    @PostMapping()
    public ResponseEntity<ItemResponse> uploadItem(@Valid @RequestBody UploadItemRequest request) {
        return ResponseEntity.ok(itemService.uploadItem(request));
    }

    @SecurityRequirement(name = AppConstants.SEC_REQ_NAME)
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    @PutMapping()
    public ResponseEntity<ItemResponse> updateItem(@Valid @RequestBody UpdateItemRequest request) {
        return ResponseEntity.ok(itemService.updateItem(request));
    }

    @SecurityRequirement(name = AppConstants.SEC_REQ_NAME)
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("/pending")
    public ResponseEntity<List<ItemResponse>> getAllPendingItem() {
        return ResponseEntity.ok(itemService.getAllPendingItem());
    }

    @SecurityRequirement(name = AppConstants.SEC_REQ_NAME)
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("/review")
    public ResponseEntity<ItemResponse> reviewItem(@RequestParam("id") Integer id, @RequestParam("statusItem") StatusItem statusItem) {
        return ResponseEntity.ok(itemService.reviewItem(id, statusItem));
    }
}

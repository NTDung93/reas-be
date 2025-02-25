package vn.fptu.reasbe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.user.CreateStaffRequest;
import vn.fptu.reasbe.model.dto.user.SearchUserRequest;
import vn.fptu.reasbe.model.dto.user.UpdateStaffRequest;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.service.UserService;

/**
 *
 * @author ntig
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STAFF')")
    @PostMapping("/search")
    public ResponseEntity<BaseSearchPaginationResponse<UserResponse>> searchUserPagination(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestBody @Nullable SearchUserRequest request
    ) {
        return ResponseEntity.ok(userService.searchUserPagination(pageNo, pageSize, sortBy, sortDir, request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create-new-staff")
    public ResponseEntity<UserResponse> createNewStaff(@RequestBody @Valid CreateStaffRequest request) {
        return ResponseEntity.ok(userService.createNewStaff(request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-staff")
    public ResponseEntity<UserResponse> updateStaff(@RequestBody @Valid UpdateStaffRequest request) {
        return ResponseEntity.ok(userService.updateStaff(request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deactivate-staff/{userId}")
    public ResponseEntity<Boolean> deactivateStaff(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.deactivateStaff(userId));
    }
}

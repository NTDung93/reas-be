package vn.fptu.reasbe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationDto;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationRequest;
import vn.fptu.reasbe.service.UserLocationService;
import vn.fptu.reasbe.utils.mapper.UserLocationMapper;

@RestController
@RequestMapping("/api/v1/user-location")
@RequiredArgsConstructor
public class UserLocationController {

    private final UserLocationService userLocationService;
    private final UserLocationMapper userLocationMapper;

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    @GetMapping
    public ResponseEntity<BaseSearchPaginationResponse<UserLocationDto>> getAllUserLocationOfCurrentUser(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(userLocationService.getAllUserLocationOfCurrentUser(pageNo, pageSize, sortBy, sortDir));
    }

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    @GetMapping("/{id}")
    public ResponseEntity<UserLocationDto> getUserLocationDetailOfCurrentUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userLocationMapper.toDto(userLocationService.getUserLocationOfCurrentUserById(id)));
    }

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    @PostMapping
    public ResponseEntity<UserLocationDto> createNewUserLocation(@Valid @RequestBody UserLocationRequest request) {
        return ResponseEntity.ok(userLocationService.createNewUserLocation(request));
    }

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    @PutMapping
    public ResponseEntity<UserLocationDto> updateUserLocationOfCurrentUser(@Valid @RequestBody UserLocationRequest request) {
        return ResponseEntity.ok(userLocationService.updateUserLocationOfCurrentUser(request));
    }

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_RESIDENT)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUserLocationOfCurrentUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userLocationService.deleteUserLocationOfCurrentUser(id));
    }
}

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
import vn.fptu.reasbe.model.dto.subscriptionplan.SearchSubscriptionPlanRequest;
import vn.fptu.reasbe.model.dto.subscriptionplan.SubscriptionPlanDto;
import vn.fptu.reasbe.service.SubscriptionPlanService;

/**
 *
 * @author dungnguyen
 */
@RestController
@RequestMapping("/api/v1/subscription-plan")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping("/search")
    public ResponseEntity<BaseSearchPaginationResponse<SubscriptionPlanDto>> searchSubscriptionPlanPagination(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestBody @Nullable SearchSubscriptionPlanRequest request
    ) {
        return ResponseEntity.ok(subscriptionPlanService.searchSubscriptionPlanPagination(pageNo, pageSize, sortBy, sortDir, request));
    }

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_ADMIN)")
    @PostMapping
    public ResponseEntity<SubscriptionPlanDto> createNewSubscriptionPlan(@RequestBody @Valid SubscriptionPlanDto request) {
        return ResponseEntity.ok(subscriptionPlanService.createSubscriptionPlan(request));
    }

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_ADMIN)")
    @PutMapping
    public ResponseEntity<SubscriptionPlanDto> updateSubscriptionPlan(@RequestBody @Valid SubscriptionPlanDto request) {
        return ResponseEntity.ok(subscriptionPlanService.updateSubscriptionPlan(request));
    }

    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_ADMIN)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deactivateSubscriptionPlan(@PathVariable Integer id) {
        return ResponseEntity.ok(subscriptionPlanService.deactivateSubscriptionPlan(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDto> getSubscriptionPlanById(@PathVariable Integer id) {
        return ResponseEntity.ok(subscriptionPlanService.getSubscriptionPlanById(id));
    }
}

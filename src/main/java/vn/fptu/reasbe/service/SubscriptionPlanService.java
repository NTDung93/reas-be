package vn.fptu.reasbe.service;

import java.util.List;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.subscriptionplan.SearchSubscriptionPlanRequest;
import vn.fptu.reasbe.model.dto.subscriptionplan.SubscriptionPlanDto;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;

/**
 *
 * @author dungnguyen
 */
public interface SubscriptionPlanService {
    List<SubscriptionPlanDto> getAllSubscriptionPlans();
    BaseSearchPaginationResponse<SubscriptionPlanDto> searchSubscriptionPlanPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchSubscriptionPlanRequest search);
    SubscriptionPlanDto createSubscriptionPlan(SubscriptionPlanDto subscriptionPlanDto);
    SubscriptionPlanDto updateSubscriptionPlan(SubscriptionPlanDto subscriptionPlanDto);
    SubscriptionPlan getSubscriptionPlanByPlanId(Integer id);
    Boolean deactivateSubscriptionPlan(Integer id);
    SubscriptionPlan getSubscriptionPlanTypeExtension();
}

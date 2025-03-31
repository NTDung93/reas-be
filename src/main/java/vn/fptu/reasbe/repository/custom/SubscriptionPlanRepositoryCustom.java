package vn.fptu.reasbe.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.fptu.reasbe.model.dto.subscriptionplan.SearchSubscriptionPlanRequest;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;

/**
 *
 * @author ntig
 */
public interface SubscriptionPlanRepositoryCustom {
    Page<SubscriptionPlan> searchSubscriptionPlanPagination(SearchSubscriptionPlanRequest request, Pageable pageable);
}

package vn.fptu.reasbe.repository.custom.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import vn.fptu.reasbe.model.dto.subscriptionplan.SearchSubscriptionPlanRequest;
import vn.fptu.reasbe.model.entity.QSubscriptionPlan;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.repository.custom.SubscriptionPlanRepositoryCustom;
import vn.fptu.reasbe.repository.custom.core.AbstractRepositoryCustom;

/**
 *
 * @author ntig
 */
public class SubscriptionPlanRepositoryCustomImpl extends AbstractRepositoryCustom<SubscriptionPlan, QSubscriptionPlan> implements SubscriptionPlanRepositoryCustom {

    private static final Logger logger = Logger.getLogger(SubscriptionPlanRepositoryCustomImpl.class);

    @Override
    protected QSubscriptionPlan getEntityPath() {
        return QSubscriptionPlan.subscriptionPlan;
    }

    @Override
    protected <T> Expression<T> getIdPath(QSubscriptionPlan entityPath) {
        return (Expression<T>) entityPath.id;
    }

    @Override
    public Page<SubscriptionPlan> searchSubscriptionPlanPagination(SearchSubscriptionPlanRequest request, Pageable pageable) {
        return super.searchPagination(request, pageable);
    }

    @Override
    protected BooleanBuilder buildFilter(Object requestObj, QSubscriptionPlan entityPath) {
        QSubscriptionPlan subscriptionPlan = getEntityPath();
        BooleanBuilder builder = new BooleanBuilder();
        SearchSubscriptionPlanRequest request = (SearchSubscriptionPlanRequest) requestObj;

        if (request != null) {
            if (StringUtils.isNotBlank(request.getName())) {
                builder.and(subscriptionPlan.name.containsIgnoreCase(request.getName().trim()));
            }
            if (StringUtils.isNotBlank(request.getDescription())) {
                builder.and(subscriptionPlan.description.containsIgnoreCase(request.getDescription().trim()));
            }
            if (request.getPrice() != null) {
                builder.and(subscriptionPlan.price.eq(request.getPrice()));
            }
            if (request.getFromPrice() != null && request.getToPrice() != null) {
                builder.and(subscriptionPlan.price.between(request.getFromPrice(), request.getToPrice()));
            } else if (request.getFromPrice() == null && request.getToPrice() != null) {
                builder.and(subscriptionPlan.price.loe(request.getToPrice()));
            } else if (request.getFromPrice() != null && request.getToPrice() == null) {
                builder.and(subscriptionPlan.price.goe(request.getFromPrice()));
            }
            if (request.getTypeSubscriptionPlans() != null && !request.getTypeSubscriptionPlans().isEmpty()) {
                builder.and(subscriptionPlan.typeSubscriptionPlan.in(request.getTypeSubscriptionPlans()));
            }
            if (request.getStatusEntities() != null && !request.getStatusEntities().isEmpty()) {
                builder.and(subscriptionPlan.statusEntity.in(request.getStatusEntities()));
            }
            if (request.getDuration() != null) {
                builder.and(subscriptionPlan.duration.eq(request.getDuration()));
            }
            if (request.getFromDuration() != null && request.getToDuration() != null) {
                builder.and(subscriptionPlan.duration.between(request.getFromDuration(), request.getToDuration()));
            } else if (request.getFromDuration() == null && request.getToDuration() != null) {
                builder.and(subscriptionPlan.duration.loe(request.getToDuration()));
            } else if (request.getFromDuration() != null && request.getToDuration() == null) {
                builder.and(subscriptionPlan.duration.goe(request.getFromDuration()));
            }
        } else {
            builder.and(subscriptionPlan.statusEntity.eq(StatusEntity.ACTIVE));
        }

        return builder;
    }

    @Override
    protected OrderSpecifier<?> mapSortPropertyToOrderSpecifier(String property, Sort.Direction direction, QSubscriptionPlan subscriptionPlan) {
        switch (property) {
            case "id":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, subscriptionPlan.id);
            case "name":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, subscriptionPlan.name);
            case "description":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, subscriptionPlan.description);
            case "price":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, subscriptionPlan.price);
            case "typeSubscriptionPlan":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, subscriptionPlan.typeSubscriptionPlan);
            case "duration":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, subscriptionPlan.duration);
            case "statusEntity":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, subscriptionPlan.statusEntity);
            default:
                logger.warn("Unknown sort property: " + property);
                return null;
        }
    }
}


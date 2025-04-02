package vn.fptu.reasbe.repository.custom.impl;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import vn.fptu.reasbe.model.dto.paymenthistory.SearchPaymentHistoryRequest;
import vn.fptu.reasbe.model.entity.PaymentHistory;
import vn.fptu.reasbe.model.entity.QPaymentHistory;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.repository.custom.PaymentHistoryRepositoryCustom;
import vn.fptu.reasbe.repository.custom.core.AbstractRepositoryCustom;

/**
 *
 * @author dungnguyen
 */
public class PaymentHistoryRepositoryCustomImpl extends AbstractRepositoryCustom<PaymentHistory, QPaymentHistory> implements PaymentHistoryRepositoryCustom {

    private static final Logger logger = Logger.getLogger(PaymentHistoryRepositoryCustomImpl.class);

    @Override
    protected QPaymentHistory getEntityPath() {
        return QPaymentHistory.paymentHistory;
    }

    @Override
    protected <T> Expression<T> getIdPath(QPaymentHistory entityPath) {
        return (Expression<T>) entityPath.id;
    }

    @Override
    public Page<PaymentHistory> searchPaymentHistoryPagination(SearchPaymentHistoryRequest request, Pageable pageable) {
        return super.searchPagination(request, pageable);
    }

    @Override
    protected BooleanBuilder buildFilter(Object requestObj, QPaymentHistory entityPath) {
        QPaymentHistory paymentHistory = getEntityPath();
        BooleanBuilder builder = new BooleanBuilder();
        SearchPaymentHistoryRequest request = (SearchPaymentHistoryRequest) requestObj;

        if (request != null) {
            if (request.getUserId() != null) {
                builder.and(paymentHistory.userSubscription.user.id.eq(request.getUserId()));
            }
            if (request.getTransactionId() != null) {
                builder.and(paymentHistory.transactionId.eq(request.getTransactionId()));
            }
            if (request.getPrice() != null) {
                builder.and(paymentHistory.amount.eq(request.getPrice()));
            }
            if (request.getFromPrice() != null && request.getToPrice() != null) {
                builder.and(paymentHistory.amount.between(request.getFromPrice(), request.getToPrice()));
            } else if (request.getFromPrice() == null && request.getToPrice() != null) {
                builder.and(paymentHistory.amount.loe(request.getToPrice()));
            } else if (request.getFromPrice() != null && request.getToPrice() == null) {
                builder.and(paymentHistory.amount.goe(request.getFromPrice()));
            }
            if (request.getStatusPayments() != null && !request.getStatusPayments().isEmpty()) {
                builder.and(paymentHistory.statusPayment.in(request.getStatusPayments()));
            }
            if (request.getMethodPayments() != null && !request.getMethodPayments().isEmpty()) {
                builder.and(paymentHistory.methodPayment.in(request.getMethodPayments()));
            }
            if (request.getStatusEntities() != null && !request.getStatusEntities().isEmpty()) {
                builder.and(paymentHistory.statusEntity.in(request.getStatusEntities()));
            }
            if (request.getFromTransactionDate() != null && request.getToTransactionDate() != null) {
                builder.and(paymentHistory.transactionDateTime.between(request.getFromTransactionDate(), request.getToTransactionDate()));
            } else if (request.getFromTransactionDate() == null && request.getToTransactionDate() != null) {
                builder.and(paymentHistory.transactionDateTime.loe(request.getToTransactionDate()));
            } else if (request.getFromTransactionDate() != null && request.getToTransactionDate() == null) {
                builder.and(paymentHistory.transactionDateTime.goe(request.getFromTransactionDate()));
            }
        } else {
            builder.and(paymentHistory.statusEntity.eq(StatusEntity.ACTIVE));
        }
        return builder;
    }

    @Override
    protected OrderSpecifier<?> mapSortPropertyToOrderSpecifier(String property, Sort.Direction direction, QPaymentHistory paymentHistory) {
        switch (property) {
            case "transactionId":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, paymentHistory.transactionId);
            case "amount":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, paymentHistory.amount);
            case "transactionDateTime":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, paymentHistory.transactionDateTime);
            case "description":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, paymentHistory.description);
            case "statusPayment":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, paymentHistory.statusPayment);
            case "methodPayment":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, paymentHistory.methodPayment);
            default:
                logger.warn("Invalid sort property: " + property);
                return null;
        }
    }
}

package vn.fptu.reasbe.repository.custom.core;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author ntig
 */
public abstract class AbstractRepositoryCustom<T, Q extends EntityPathBase<T>>{
    @PersistenceContext
    protected EntityManager em;

    protected abstract Q getEntityPath();
    protected abstract <T> Expression<T> getIdPath(Q entityPath);
    protected abstract BooleanBuilder buildFilter(Object request, Q entityPath);
    protected abstract OrderSpecifier<?> mapSortPropertyToOrderSpecifier(String property, Sort.Direction direction, Q entityPath);

    public Page<T> searchPagination(Object requestObj, Pageable pageable) {
        Q entityPath = getEntityPath();
        BooleanBuilder builder = buildFilter(requestObj, entityPath);

        JPAQuery<T> query = new JPAQuery<T>(em).from(entityPath).where(builder);
        Sort.Order sortOrder = pageable.getSort().iterator().next();
        String property = sortOrder.getProperty();
        Sort.Direction direction = sortOrder.getDirection();

        OrderSpecifier<?> orderSpecifier = mapSortPropertyToOrderSpecifier(property, direction, entityPath);
        if (orderSpecifier != null) {
            query.orderBy(orderSpecifier);
        } else {
            query.orderBy(getDefaultOrder(entityPath));
        }

        List<T> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = new JPAQuery<>(em).from(entityPath).where(builder).fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    protected OrderSpecifier<?> getDefaultOrder(Q entityPath) {
        return new OrderSpecifier<>(Order.ASC, getIdPath(entityPath));
    }
}

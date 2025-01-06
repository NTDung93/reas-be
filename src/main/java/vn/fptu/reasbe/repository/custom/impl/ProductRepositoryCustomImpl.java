package vn.fptu.reasbe.repository.custom.impl;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.fptu.reasbe.model.dto.product.SearchProductRequest;
import vn.fptu.reasbe.model.entity.Product;
import vn.fptu.reasbe.model.entity.QProduct;
import vn.fptu.reasbe.model.enums.EntityStatus;
import vn.fptu.reasbe.repository.custom.ProductRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author ntig
 */
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    private static Logger logger = Logger.getLogger(ProductRepositoryCustomImpl.class);

    @Override
    public Page<Product> searchProductPagination(SearchProductRequest request, Pageable pageable) {
        QProduct product = QProduct.product;
        BooleanBuilder builder = new BooleanBuilder();

        if (request != null) {
            if (StringUtils.isNotBlank(request.getProductName())) {
                builder.and(product.productName.containsIgnoreCase(request.getProductName().trim()));
            }
            if (StringUtils.isNotBlank(request.getDescription())) {
                builder.and(product.description.containsIgnoreCase(request.getDescription().trim()));
            }
            if (request.getPrice() != null) {
                builder.and(product.price.eq(request.getPrice()));
            }
            if (request.getFromPrice() != null && request.getToPrice() != null) {
                builder.and(product.price.between(request.getFromPrice(), request.getToPrice()));
            } else if (request.getFromPrice() == null && request.getToPrice() != null) {
                builder.and(product.price.loe(request.getToPrice()));
            } else if (request.getFromPrice() != null && request.getToPrice() == null) {
                builder.and(product.price.goe(request.getFromPrice()));
            }
            if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
                builder.and(product.category.id.in(request.getCategoryIds()));
            }
            if (request.getBrandIds() != null && !request.getBrandIds().isEmpty()) {
                builder.and(product.brand.id.in(request.getBrandIds()));
            }
            if (request.getProductStatuses() != null && !request.getProductStatuses().isEmpty()) {
                builder.and(product.productStatus.in(request.getProductStatuses()));
            }
            if (request.getEntityStatuses() != null && !request.getEntityStatuses().isEmpty()) {
                builder.and(product.entityStatus.in(request.getEntityStatuses()));
            }
        }else{
            // if request null, fetch all products with status ACTIVE by default
            builder.and(product.entityStatus.eq(EntityStatus.ACTIVE));
        }

        JPAQuery<Product> query = new JPAQuery<Product>(em).from(product).where(builder);
        Sort.Order sortOrder = pageable.getSort().iterator().next();
        String property = sortOrder.getProperty();
        Sort.Direction direction = sortOrder.getDirection();

        OrderSpecifier<?> orderSpecifier = mapSortPropertyToOrderSpecifier(property, direction, product);
        if (orderSpecifier != null) {
            query.orderBy(orderSpecifier);
        } else {
            query.orderBy(product.id.asc());
        }

        List<Product> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = new JPAQuery<Product>(em).from(product)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    private OrderSpecifier<?> mapSortPropertyToOrderSpecifier(String property, Sort.Direction direction, QProduct product) {
        switch (property) {
            case "id":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, product.id);
            case "productName":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, product.productName);
            case "description":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, product.description);
            case "price":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, product.price);
            case "category":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, product.category.categoryName);
            case "brand":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, product.brand.brandName);
            case "productStatus":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, product.productStatus);
            case "entityStatus":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, product.entityStatus);
            default:
                logger.warn("Unknown sort property: " + property);
                return null;
        }
    }
}

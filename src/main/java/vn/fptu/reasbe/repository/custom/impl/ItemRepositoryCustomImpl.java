package vn.fptu.reasbe.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import vn.fptu.reasbe.model.dto.item.ItemRunnerDTO;
import vn.fptu.reasbe.model.dto.item.SearchItemRequest;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.QBrand;
import vn.fptu.reasbe.model.entity.QCategory;
import vn.fptu.reasbe.model.entity.QItem;
import vn.fptu.reasbe.model.entity.QUser;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.repository.custom.ItemRepositoryCustom;
import vn.fptu.reasbe.repository.custom.core.AbstractRepositoryCustom;

import java.util.List;

/**
 *
 * @author ntig
 */
public class ItemRepositoryCustomImpl extends AbstractRepositoryCustom<Item, QItem> implements ItemRepositoryCustom {
    private static final Logger logger = Logger.getLogger(ItemRepositoryCustomImpl.class);

    @Override
    protected QItem getEntityPath() {
        return QItem.item;
    }

    @Override
    protected <T> Expression<T> getIdPath(QItem entityPath) {
        return (Expression<T>) entityPath.id;
    }

    @Override
    public Page<Item> searchItemPagination(SearchItemRequest request, Pageable pageable) {
        return super.searchPagination(request, pageable);
    }

    @Override
    public List<ItemRunnerDTO> findAllItemRunnerByStatus(StatusItem statusItem) {
        QItem item = QItem.item;
        QBrand brand = QBrand.brand;
        QCategory category = QCategory.category;
        QUser user = QUser.user;

        return new JPAQuery<ItemRunnerDTO>(em)
                .select(Projections.constructor(ItemRunnerDTO.class,
                        item.id,
                        item.itemName,
                        brand.brandName,
                        category.categoryName,
                        item.price,
                        item.description,
                        item.conditionItem,
                        user.id))
                .from(item)
                .join(item.brand, brand)
                .join(item.category, category)
                .join(item.owner, user)
                .where(item.statusItem.eq(StatusItem.AVAILABLE)
                        .and(item.statusEntity.eq(StatusEntity.ACTIVE))
                        .and(user.statusEntity.eq(StatusEntity.ACTIVE)))
                .fetch();
    }

    @Override
    protected BooleanBuilder buildFilter(Object requestObj, QItem entityPath) {
        QItem item = QItem.item;
        BooleanBuilder builder = new BooleanBuilder();
        SearchItemRequest request = (SearchItemRequest) requestObj; // cast Object to SearchItemRequest

        if (request != null) {
            if (StringUtils.isNotBlank(request.getItemName())) {
                builder.and(item.itemName.containsIgnoreCase(request.getItemName().trim()));
            }
            if (StringUtils.isNotBlank(request.getDescription())) {
                builder.and(item.description.containsIgnoreCase(request.getDescription().trim()));
            }
            if (request.getPrice() != null) {
                builder.and(item.price.eq(request.getPrice()));
            }
            if (request.getFromPrice() != null && request.getToPrice() != null) {
                builder.and(item.price.between(request.getFromPrice(), request.getToPrice()));
            } else if (request.getFromPrice() == null && request.getToPrice() != null) {
                builder.and(item.price.loe(request.getToPrice()));
            } else if (request.getFromPrice() != null && request.getToPrice() == null) {
                builder.and(item.price.goe(request.getFromPrice()));
            }
            if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
                builder.and(item.category.id.in(request.getCategoryIds()));
            }
            if (request.getBrandIds() != null && !request.getBrandIds().isEmpty()) {
                builder.and(item.brand.id.in(request.getBrandIds()));
            }
            if (request.getOwnerIds() != null && !request.getOwnerIds().isEmpty()) {
                builder.and(item.owner.id.in(request.getOwnerIds()));
            }
            if (request.getLocationIds() != null && !request.getLocationIds().isEmpty()) {
                builder.and(item.userLocation.location.id.in(request.getLocationIds()));
            }
            if (request.getStatusItems() != null && !request.getStatusItems().isEmpty()) {
                builder.and(item.statusItem.in(request.getStatusItems()));
            }
            if (request.getStatusEntities() != null && !request.getStatusEntities().isEmpty()) {
                builder.and(item.statusEntity.in(request.getStatusEntities()));
            }
            if (request.getTypeItems() != null && !request.getTypeItems().isEmpty()) {
                builder.and(item.category.typeItem.in(request.getTypeItems()));
            }
        } else {
            // if request null, fetch all items with status ACTIVE by default
            builder.and(item.statusEntity.eq(StatusEntity.ACTIVE));
        }
        return builder;
    }

    @Override
    protected OrderSpecifier<?> mapSortPropertyToOrderSpecifier(String property, Sort.Direction direction, QItem item) {
        switch (property) {
            case "id":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.id);
            case "itemName":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.itemName);
            case "description":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.description);
            case "price":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.price);
            case "category":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.category.categoryName);
            case "brand":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.brand.brandName);
            case "owner":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.owner.fullName);
            case "location":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.userLocation.location.area);
            case "statusItem":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.statusItem);
            case "statusEntity":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.statusEntity);
            case "approvedTime":
                return new OrderSpecifier<>(
                        direction.isAscending() ? Order.ASC : Order.DESC, item.approvedTime);
            default:
                logger.warn("Unknown sort property: " + property);
                return null;
        }
    }
}

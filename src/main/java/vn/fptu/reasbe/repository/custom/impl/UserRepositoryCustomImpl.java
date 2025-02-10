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

import vn.fptu.reasbe.model.dto.user.SearchUserRequest;
import vn.fptu.reasbe.model.entity.QUser;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.repository.custom.UserRepositoryCustom;
import vn.fptu.reasbe.repository.custom.core.AbstractRepositoryCustom;

/**
 *
 * @author ntig
 */
public class UserRepositoryCustomImpl extends AbstractRepositoryCustom<User, QUser> implements UserRepositoryCustom {

    private static final Logger logger = Logger.getLogger(UserRepositoryCustomImpl.class);

    @Override
    protected QUser getEntityPath() {
        return QUser.user;
    }

    @Override
    protected <T> Expression<T> getIdPath(QUser entityPath) {
        return (Expression<T>) entityPath.id;
    }

    @Override
    public Page<User> searchUserPagination(SearchUserRequest request, Pageable pageable) {
        return super.searchPagination(request, pageable);
    }

    @Override
    protected BooleanBuilder buildFilter(Object requestObj, QUser entityPath) {
        QUser user = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();
        SearchUserRequest request = (SearchUserRequest) requestObj;

        if (request != null) {
            if (StringUtils.isNotBlank(request.getUserName())) {
                builder.and(user.userName.containsIgnoreCase(request.getUserName().trim()));
            }
            if (StringUtils.isNotBlank(request.getFullName())) {
                builder.and(user.fullName.containsIgnoreCase(request.getFullName().trim()));
            }
            if (StringUtils.isNotBlank(request.getEmail())) {
                builder.and(user.email.containsIgnoreCase(request.getEmail().trim()));
            }
            if (StringUtils.isNotBlank(request.getPhone())) {
                builder.and(user.phone.containsIgnoreCase(request.getPhone().trim()));
            }
            if (request.getGenders() != null && !request.getGenders().isEmpty()) {
                builder.and(user.gender.in(request.getGenders()));
            }
            if (request.getStatusEntities() != null && !request.getStatusEntities().isEmpty()) {
                builder.and(user.statusEntity.in(request.getStatusEntities()));
            }
            if (request.getRoleNames() != null && !request.getRoleNames().isEmpty()) {
                builder.and(user.role.name.in(request.getRoleNames()));
            }
        } else {
            builder.and(user.statusEntity.eq(StatusEntity.ACTIVE));
        }
        return builder;
    }

    @Override
    protected OrderSpecifier<?> mapSortPropertyToOrderSpecifier(String property, Sort.Direction direction, QUser user) {
        switch (property) {
            case "id":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, user.id);
            case "userName":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, user.userName);
            case "fullName":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, user.fullName);
            case "email":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, user.email);
            case "phone":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, user.phone);
            case "gender":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, user.gender);
            case "status":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, user.statusEntity);
            case "roleName":
                return new OrderSpecifier<>(direction.isAscending() ? Order.ASC : Order.DESC, user.role.name);
            default:
                logger.warn("Unknown sort property: " + property);
                return null;
        }
    }
}

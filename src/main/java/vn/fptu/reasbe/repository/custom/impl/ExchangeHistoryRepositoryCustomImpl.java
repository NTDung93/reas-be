package vn.fptu.reasbe.repository.custom.impl;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import vn.fptu.reasbe.model.entity.ExchangeHistory;
import vn.fptu.reasbe.model.entity.QExchangeHistory;
import vn.fptu.reasbe.model.entity.QExchangeRequest;
import vn.fptu.reasbe.model.entity.QItem;
import vn.fptu.reasbe.model.entity.QUser;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;
import vn.fptu.reasbe.repository.custom.ExchangeHistoryRepositoryCustom;
import vn.fptu.reasbe.repository.custom.core.AbstractRepositoryCustom;
import vn.fptu.reasbe.utils.common.DateUtils;

/**
 *
 * @author dungnguyen
 */
public class ExchangeHistoryRepositoryCustomImpl extends AbstractRepositoryCustom<ExchangeHistory, QExchangeHistory> implements ExchangeHistoryRepositoryCustom {

    @PersistenceContext
    protected EntityManager em;

    @Override
    protected QExchangeHistory getEntityPath() {
        return QExchangeHistory.exchangeHistory;
    }

    @Override
    protected <T> Expression<T> getIdPath(QExchangeHistory entityPath) {
        return (Expression<T>) entityPath.id;
    }

    @Override
    protected BooleanBuilder buildFilter(Object request, QExchangeHistory entityPath) {
        return null;
    }

    @Override
    protected OrderSpecifier<?> mapSortPropertyToOrderSpecifier(String property, Sort.Direction direction, QExchangeHistory entityPath) {
        return null;
    }

    @Override
    public Integer getNumberOfSuccessfulExchangesOfUser(Integer month, Integer year, Integer userId) {
        QExchangeHistory exchangeHistory = QExchangeHistory.exchangeHistory;
        QExchangeRequest exchangeRequest = QExchangeRequest.exchangeRequest;

        // Define aliases for seller and buyer items and their owners
        QItem sellerItem = QItem.item;
        QUser sellerUser = QUser.user;
        QItem buyerItem = new QItem("buyerItem");
        QUser buyerUser = new QUser("buyerUser");

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(DateUtils.extractMonth(exchangeHistory.creationDate).eq(month))
                .and(DateUtils.extractYear(exchangeHistory.creationDate).eq(year))
                .and(exchangeHistory.statusEntity.eq(StatusEntity.ACTIVE))
                .and(exchangeHistory.statusExchangeHistory.eq(StatusExchangeHistory.SUCCESSFUL));

        Long result = new JPAQuery<>(em)
                .from(exchangeHistory)
                .leftJoin(exchangeHistory.exchangeRequest, exchangeRequest)
                // Join seller item and its owner
                .leftJoin(exchangeRequest.sellerItem, sellerItem)
                .leftJoin(sellerItem.owner, sellerUser)
                // Join buyer item and its owner
                .leftJoin(exchangeRequest.buyerItem, buyerItem)
                .leftJoin(buyerItem.owner, buyerUser)
                .where(builder.and(
                        sellerUser.id.eq(userId).or(buyerUser.id.eq(userId))
                ))
                .select(exchangeHistory.count())
                .fetchOne();

        return Optional.ofNullable(result)
                .map(Long::intValue)
                .orElse(0);
    }
}

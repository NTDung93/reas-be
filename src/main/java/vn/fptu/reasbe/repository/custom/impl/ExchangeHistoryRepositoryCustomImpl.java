package vn.fptu.reasbe.repository.custom.impl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
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

    private static final QExchangeHistory exchangeHistory = QExchangeHistory.exchangeHistory;
    private static final QExchangeRequest exchangeRequest = QExchangeRequest.exchangeRequest;
    private static final QItem sellerItem = QItem.item;
    private static final QItem buyerItem = new QItem("buyerItem");
    private static final QUser sellerUser = QUser.user;
    private static final QUser buyerUser = new QUser("buyerUser");

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
        BooleanBuilder builder = createBaseExchangeFilter(month, year)
                .and(sellerUser.id.eq(userId).or(buyerUser.id.eq(userId)));

        Long result = createQuery()
                .where(builder)
                .select(exchangeHistory.count())
                .fetchOne();

        return Optional.ofNullable(result)
                .map(Long::intValue)
                .orElse(0);
    }

    @Override
    public BigDecimal getRevenueOfUserInOneYearFromExchanges(Integer year, Integer userId) {
        BooleanBuilder builder = createBaseExchangeFilter(null, year)
                .and(exchangeRequest.paidBy.id.ne(userId))
                .and(sellerItem.owner.id.eq(userId).or(buyerItem.owner.id.eq(userId)));

        BigDecimal result = createQuery()
                .where(builder)
                .select(exchangeRequest.finalPrice.sum())
                .fetchOne();

        return Optional.ofNullable(result)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public Map<Integer, BigDecimal> getMonthlyRevenueOfUserInOneYearFromExchanges(Integer year, Integer userId) {
        BooleanBuilder builder = createBaseExchangeFilter(null, year)
                .and(exchangeRequest.paidBy.id.ne(userId))
                .and(sellerItem.owner.id.eq(userId).or(buyerItem.owner.id.eq(userId)));

        NumberExpression<Integer> monthExpression = DateUtils.extractMonth(exchangeHistory.lastModificationDate);

        return createQuery()
                .where(builder)
                .groupBy(monthExpression)
                .select(Projections.tuple(monthExpression, exchangeRequest.finalPrice.sum()))
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(monthExpression),
                        tuple -> Optional.ofNullable(tuple.get(exchangeRequest.finalPrice.sum())).orElse(BigDecimal.ZERO)
                ));
    }

    private BooleanBuilder createBaseExchangeFilter(Integer month, Integer year) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(exchangeHistory.statusEntity.eq(StatusEntity.ACTIVE))
                .and(exchangeHistory.statusExchangeHistory.eq(StatusExchangeHistory.SUCCESSFUL));

        if (Objects.nonNull(month)) {
            builder.and(DateUtils.extractMonth(exchangeHistory.creationDate).eq(month));
        }
        if (Objects.nonNull(year)) {
            builder.and(DateUtils.extractYear(exchangeHistory.creationDate).eq(year));
        }
        return builder;
    }

    private JPAQuery<?> createQuery() {
        return new JPAQuery<>(em)
                .from(exchangeHistory)
                .leftJoin(exchangeHistory.exchangeRequest, exchangeRequest)
                .leftJoin(exchangeRequest.sellerItem, sellerItem)
                .leftJoin(exchangeRequest.buyerItem, buyerItem)
                .leftJoin(sellerItem.owner, sellerUser)
                .leftJoin(buyerItem.owner, buyerUser);
    }
}
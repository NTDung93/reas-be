package vn.fptu.reasbe.repository;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import org.springframework.data.repository.query.Param;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.repository.custom.ItemRepositoryCustom;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ntig
 */

public interface ItemRepository extends JpaRepository<Item, Integer>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    Page<Item> findAllByStatusItem(StatusItem statusItem, Pageable pageable);

    Page<Item> findAllByOwnerIdAndStatusItemOrderByCreationDateDesc(Integer ownerId, StatusItem statusItem, Pageable pageable);

    List<Item> findAllByExpiredTimeBeforeAndStatusItem(LocalDateTime currDateTime, StatusItem statusItem);

    List<Item> findAllByStatusItem(StatusItem statusItem);

    List<Item> findByStatusItemAndOwnerIdAndIdNotOrderByApprovedTimeDesc(StatusItem statusItem, Integer ownerId, Integer itemId, Pageable pageable);

    boolean existsByIdAndStatusItemEqualsAndStatusEntityEquals(Integer itemId, StatusItem statusItem, StatusEntity statusEntity);

    @Query(value = """
    SELECT i.*
    FROM public."ITEM" i
    LEFT JOIN public."USER_LOCATION" ul ON i."USER_LOCATION_ID" = ul."USER_LOCATION_ID"
    WHERE ST_DWithin(ul."POINT", :referencePoint, :distance, true)
    AND i."STATUS_ITEM" = :statusItem
    ORDER BY ST_Distance(ul."POINT", :referencePoint) ASC
    """, nativeQuery = true)
    List<Item> findNearbyItems(@Param("referencePoint") Point referencePoint,
                               @Param("distance") double distance,
                               @Param("statusItem") String statusItem);

    Integer countByOwnerAndStatusItemInAndCreationDateBetween(User user, List<StatusItem> statusItems, LocalDateTime from, LocalDateTime to);
}

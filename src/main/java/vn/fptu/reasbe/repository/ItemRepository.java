package vn.fptu.reasbe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.fptu.reasbe.model.entity.Item;
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

    List<Item> findByStatusItemAndOwnerIdAndIdNotOrderByApprovedTimeDesc(StatusItem statusItem, Integer ownerId, Integer itemId, Pageable pageable);}

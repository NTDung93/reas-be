package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.repository.custom.ItemRepositoryCustom;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author ntig
 */
public interface ItemRepository extends JpaRepository<Item, Integer>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    Optional<Item> findAllByStatusItem(StatusItem statusItem);
    List<Item> findAllByOwnerId(Integer ownerId);
    List<Item> findAllByOwnerIdAndStatusItemOrderByCreationDateDesc(Integer ownerId, StatusItem statusItem);
}

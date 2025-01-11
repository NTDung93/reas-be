package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.repository.custom.ItemRepositoryCustom;

/**
 *
 * @author ntig
 */
public interface ItemRepository extends JpaRepository<Item, Integer>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
}

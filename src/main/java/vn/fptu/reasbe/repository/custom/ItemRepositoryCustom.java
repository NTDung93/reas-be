package vn.fptu.reasbe.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.fptu.reasbe.model.dto.item.SearchItemRequest;
import vn.fptu.reasbe.model.entity.Item;

import java.util.List;

/**
 *
 * @author ntig
 */
public interface ItemRepositoryCustom {
    Page<Item> searchItemPagination(SearchItemRequest request, Pageable pageable);
    List<Item> findItemsByUserIdAndOrderedByCreationDate(Integer userId);
}

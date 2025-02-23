package vn.fptu.reasbe.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.item.CreateItemRequest;
import vn.fptu.reasbe.model.dto.item.ItemResponse;
import vn.fptu.reasbe.model.dto.item.SearchItemRequest;
import vn.fptu.reasbe.model.dto.item.SearchItemResponse;
import vn.fptu.reasbe.repository.ItemRepository;
import vn.fptu.reasbe.service.ItemService;
import vn.fptu.reasbe.utils.mapper.ItemMapper;

/**
 *
 * @author ntig
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public BaseSearchPaginationResponse<SearchItemResponse> searchItemPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchItemRequest request) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return BaseSearchPaginationResponse.of(itemRepository.searchItemPagination(request, pageable).map(itemMapper::toSearchItemResponse));
    }

    @Override
    public ItemResponse createItem(CreateItemRequest request) {
        return null;
    }
}

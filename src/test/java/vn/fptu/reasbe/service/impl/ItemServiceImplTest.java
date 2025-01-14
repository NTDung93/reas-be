package vn.fptu.reasbe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.item.SearchItemRequest;
import vn.fptu.reasbe.model.dto.item.SearchItemResponse;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.repository.ItemRepository;
import vn.fptu.reasbe.utils.mapper.ItemMapper;

/**
 *
 * @author ntig
 */
@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemServiceImpl;

    private SearchItemRequest searchItemRequest;
    private SearchItemResponse searchItemResponse;
    private Item item;

    @BeforeEach
    public void setUp() {
        searchItemRequest = new SearchItemRequest();
        searchItemRequest.setItemName("Samsung Galaxy S21");
        searchItemRequest.setDescription("Latest model");
        searchItemRequest.setPrice(new BigDecimal("799.99"));
        searchItemRequest.setFromPrice(new BigDecimal("700.00"));
        searchItemRequest.setToPrice(new BigDecimal("900.00"));

        searchItemResponse = new SearchItemResponse();
        searchItemResponse.setId(1);
        searchItemResponse.setItemName("Samsung Galaxy S21");
        searchItemResponse.setDescription("Latest model");
        searchItemResponse.setPrice(new BigDecimal("799.99"));
        searchItemResponse.setImageUrl("http://example.com/image.jpg");

        item = new Item();
        item.setId(1);
        item.setItemName("Samsung Galaxy S21");
        item.setDescription("Latest model");
        item.setPrice(new BigDecimal("799.99"));
        item.setImageUrl("http://example.com/image.jpg");
    }

    @Test
    public void testSearchItemPagination() {
        // Arrange
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "itemName";
        String sortDir = "ASC";

        List<Item> items = Collections.singletonList(item);
        Page<Item> itemPage = new PageImpl<>(items);

        when(itemRepository.searchItemPagination(any(SearchItemRequest.class), any(Pageable.class))).thenReturn(itemPage);
        when(itemMapper.toSearchItemResponse(any(Item.class))).thenReturn(searchItemResponse);

        // Act
        BaseSearchPaginationResponse<SearchItemResponse> response = itemServiceImpl.searchItemPagination(pageNo, pageSize, sortBy, sortDir, searchItemRequest);

        // Assert
        assertEquals(1, response.getContent().size());
        assertEquals("Samsung Galaxy S21", response.getContent().get(0).getItemName());
        assertEquals("Latest model", response.getContent().get(0).getDescription());
        assertEquals(new BigDecimal("799.99"), response.getContent().get(0).getPrice());
        assertEquals("http://example.com/image.jpg", response.getContent().get(0).getImageUrl());

        verify(itemRepository, times(1)).searchItemPagination(any(SearchItemRequest.class), any(Pageable.class));
        verify(itemMapper, times(1)).toSearchItemResponse(any(Item.class));
    }
}

package vn.fptu.reasbe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.desireditem.DesiredItemDto;
import vn.fptu.reasbe.model.dto.item.ItemResponse;
import vn.fptu.reasbe.model.dto.item.SearchItemRequest;
import vn.fptu.reasbe.model.dto.item.SearchItemResponse;
import vn.fptu.reasbe.model.dto.item.UpdateItemRequest;
import vn.fptu.reasbe.model.dto.item.UploadItemRequest;
import vn.fptu.reasbe.model.entity.Brand;
import vn.fptu.reasbe.model.entity.Category;
import vn.fptu.reasbe.model.entity.DesiredItem;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.DesiredItemRepository;
import vn.fptu.reasbe.repository.ItemRepository;
import vn.fptu.reasbe.repository.UserRepository;
import vn.fptu.reasbe.service.BrandService;
import vn.fptu.reasbe.service.CategoryService;
import vn.fptu.reasbe.utils.mapper.DesiredItemMapper;
import vn.fptu.reasbe.utils.mapper.ItemMapper;

/**
 * @author ntig
 */
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private DesiredItemMapper desiredItemMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BrandService brandService;

    @Mock
    private DesiredItemRepository desiredItemRepository;

    @InjectMocks
    private ItemServiceImpl itemServiceImpl;

    private SearchItemRequest searchItemRequest;
    private SearchItemResponse searchItemResponse;
    private Item item;
    private User mockUser;
    private Item mockItem;
    private UploadItemRequest mockUploadRequest;
    private ItemResponse mockItemResponse;
    private DesiredItemDto mockDesiredItemDto;
    private String username = "testUsername";

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

        mockUser = new User();
        mockUser.setId(1);
        mockUser.setUserName("householdUser");

        // Mock Item (Household Item)
        mockItem = new Item();
        mockItem.setId(1);
        mockItem.setItemName("Vacuum Cleaner");
        mockItem.setStatusItem(StatusItem.PENDING);

        // Mock DTO Request
        mockUploadRequest = new UploadItemRequest();
        mockUploadRequest.setItemName("Vacuum Cleaner");
        mockUploadRequest.setCategoryId(2); // Household Category
        mockUploadRequest.setBrandId(3); // Brand ID

        // Mock Response DTO
        mockItemResponse = new ItemResponse();
        mockItemResponse.setId(1);
        mockItemResponse.setItemName("Vacuum Cleaner");

        // Mock Desired Item
        mockDesiredItemDto = new DesiredItemDto();
        mockDesiredItemDto.setCategoryId(2);
        mockDesiredItemDto.setBrandId(3);
        mockDesiredItemDto.setMinPrice(BigDecimal.valueOf(50));
        mockDesiredItemDto.setMaxPrice(BigDecimal.valueOf(200));

        mockUploadRequest.setDesiredItem(mockDesiredItemDto);
    }

    @Test
    void testSearchItemPagination() {
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

    @Test
    void testUploadItem_Success() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);

        when(itemMapper.toEntity(mockUploadRequest)).thenReturn(mockItem);
        when(categoryService.getCategoryById(anyInt())).thenReturn(new Category());
        when(brandService.getBrandById(anyInt())).thenReturn(new Brand());
        when(itemRepository.save(any(Item.class))).thenReturn(mockItem);
        when(itemMapper.toItemResponse(any(Item.class))).thenReturn(mockItemResponse);
        when(desiredItemMapper.toDesiredItem(mockUploadRequest.getDesiredItem())).thenReturn(new DesiredItem());
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(mockUser));
        ItemResponse response = itemServiceImpl.uploadItem(mockUploadRequest);

        assertNotNull(response);
        assertEquals(mockItem.getItemName(), response.getItemName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testUploadItem_InvalidCategory_ThrowsException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);

        when(categoryService.getCategoryById(anyInt())).thenThrow(new ResourceNotFoundException("Category", "id", 2));
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(mockUser));
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            itemServiceImpl.uploadItem(mockUploadRequest);
        });

        assertTrue(exception.getMessage().contains("Category"));
    }

    @Test
    void testGetItemDetail_Success() {
        when(itemRepository.findById(1)).thenReturn(Optional.of(mockItem));
        when(itemMapper.toItemResponse(mockItem)).thenReturn(mockItemResponse);

        ItemResponse response = itemServiceImpl.getItemDetail(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test
    void testGetItemDetail_ItemNotFound_ThrowsException() {
        when(itemRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            itemServiceImpl.getItemDetail(1);
        });

        assertTrue(exception.getMessage().contains("Item"));
    }

    @Test
    void testUpdateItem_Success() {
        UpdateItemRequest updateRequest = new UpdateItemRequest();
        updateRequest.setId(1);
        updateRequest.setItemName("Rice Cooker");

        when(itemRepository.findById(1)).thenReturn(Optional.of(mockItem));
        when(itemRepository.save(mockItem)).thenReturn(mockItem);
        when(itemMapper.toItemResponse(mockItem)).thenReturn(mockItemResponse);

        ItemResponse response = itemServiceImpl.updateItem(updateRequest);

        assertNotNull(response);
        verify(itemRepository, times(1)).save(mockItem);
    }

    @Test
    void testUpdateItem_MinPriceGreaterThanMaxPrice_ThrowsException() {
        UpdateItemRequest updateRequest = new UpdateItemRequest();
        updateRequest.setId(1);
        updateRequest.setItemName("Rice Cooker");
        DesiredItemDto desiredItemDto = new DesiredItemDto();
        desiredItemDto.setMinPrice(BigDecimal.valueOf(20));
        desiredItemDto.setMaxPrice(BigDecimal.valueOf(10));
        updateRequest.setDesiredItem(desiredItemDto);

        when(itemRepository.findById(1)).thenReturn(Optional.of(mockItem));

        Exception exception = assertThrows(ReasApiException.class, () -> {
            itemServiceImpl.updateItem(updateRequest);
        });

        assertTrue(exception.getMessage().contains("Min price must not be greater than max price."));
    }

    @Test
    void testUpdateItem_WithRemoveDesiredItem_Success() {
        UpdateItemRequest updateRequest = new UpdateItemRequest();
        updateRequest.setId(1);
        updateRequest.setItemName("Rice Cooker");
        mockItem.setDesiredItem(new DesiredItem());

        when(itemRepository.findById(1)).thenReturn(Optional.of(mockItem));
        when(itemRepository.save(mockItem)).thenReturn(mockItem);
        when(itemMapper.toItemResponse(mockItem)).thenReturn(mockItemResponse);

        ItemResponse response = itemServiceImpl.updateItem(updateRequest);

        assertNotNull(response);
        verify(itemRepository, times(1)).save(mockItem);
    }

    @Test
    void testUpdateItem_WithUpdateDesiredItem_Success() {
        UpdateItemRequest updateRequest = new UpdateItemRequest();
        updateRequest.setId(1);
        updateRequest.setItemName("Rice Cooker");
        updateRequest.setDesiredItem(mockDesiredItemDto);
        mockItem.setDesiredItem(new DesiredItem());

        when(itemRepository.findById(1)).thenReturn(Optional.of(mockItem));
        when(itemRepository.save(mockItem)).thenReturn(mockItem);
        when(itemMapper.toItemResponse(mockItem)).thenReturn(mockItemResponse);

        ItemResponse response = itemServiceImpl.updateItem(updateRequest);

        assertNotNull(response);
        verify(itemRepository, times(1)).save(mockItem);
    }

    @Test
    void testUpdateItem_WithCreateNewDesiredItem_Success() {
        UpdateItemRequest updateRequest = new UpdateItemRequest();
        updateRequest.setId(1);
        updateRequest.setItemName("Rice Cooker");
        updateRequest.setDesiredItem(mockDesiredItemDto);
        mockItem.setDesiredItem(null);

        when(desiredItemMapper.toDesiredItem(mockDesiredItemDto)).thenReturn(new DesiredItem());
        when(itemRepository.findById(1)).thenReturn(Optional.of(mockItem));
        when(itemRepository.save(mockItem)).thenReturn(mockItem);
        when(itemMapper.toItemResponse(mockItem)).thenReturn(mockItemResponse);

        ItemResponse response = itemServiceImpl.updateItem(updateRequest);

        assertNotNull(response);
        verify(itemRepository, times(1)).save(mockItem);
    }

    @Test
    void testUpdateItem_ItemNotFound_ThrowsException() {
        UpdateItemRequest updateRequest = new UpdateItemRequest();
        updateRequest.setId(1);

        when(itemRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            itemServiceImpl.updateItem(updateRequest);
        });

        assertTrue(exception.getMessage().contains("Item"));
    }

    @Test
    void testReviewItem_Approve_Success() {
        when(itemRepository.findById(1)).thenReturn(Optional.of(mockItem));
        when(itemMapper.toItemResponse(mockItem)).thenReturn(mockItemResponse);
        when(itemRepository.save(any(Item.class))).thenReturn(mockItem);

        ItemResponse response = itemServiceImpl.reviewItem(1, StatusItem.APPROVED);

        assertNotNull(response);
        assertEquals(StatusItem.APPROVED, mockItem.getStatusItem());
        verify(itemRepository, times(1)).save(mockItem);
    }

    @Test
    void testReviewItem_Rejected_Success() {
        when(itemRepository.findById(1)).thenReturn(Optional.of(mockItem));
        when(itemMapper.toItemResponse(mockItem)).thenReturn(mockItemResponse);
        when(itemRepository.save(any(Item.class))).thenReturn(mockItem);

        ItemResponse response = itemServiceImpl.reviewItem(1, StatusItem.REJECTED);

        assertNotNull(response);
        assertEquals(StatusItem.REJECTED, mockItem.getStatusItem());
        verify(itemRepository, times(1)).save(mockItem);
    }

    @Test
    void testReviewItem_StatusNotPending_ThrowsException() {
        mockItem.setStatusItem(StatusItem.APPROVED);
        when(itemRepository.findById(1)).thenReturn(Optional.of(mockItem));

        Exception exception = assertThrows(ReasApiException.class, () -> {
            itemServiceImpl.reviewItem(1, StatusItem.REJECTED);
        });

        assertTrue(exception.getMessage().contains("Only item with PENDING"));
    }
}
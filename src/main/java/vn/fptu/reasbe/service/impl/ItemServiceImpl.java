package vn.fptu.reasbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.desireditem.DesiredItemDto;
import vn.fptu.reasbe.model.dto.goongio.DistanceMatrixResponse;
import vn.fptu.reasbe.model.dto.item.ItemResponse;
import vn.fptu.reasbe.model.dto.item.SearchItemRequest;
import vn.fptu.reasbe.model.dto.item.SearchItemResponse;
import vn.fptu.reasbe.model.dto.item.UpdateItemRequest;
import vn.fptu.reasbe.model.dto.item.UploadItemRequest;
import vn.fptu.reasbe.model.entity.DesiredItem;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.model.enums.item.TypeExchange;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.DesiredItemRepository;
import vn.fptu.reasbe.repository.ItemRepository;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.BrandService;
import vn.fptu.reasbe.service.CategoryService;
import vn.fptu.reasbe.service.ItemService;
import vn.fptu.reasbe.service.UserService;
import vn.fptu.reasbe.service.UserSubscriptionService;
import vn.fptu.reasbe.service.VectorStoreService;
import vn.fptu.reasbe.utils.common.DateUtils;
import vn.fptu.reasbe.utils.common.GeometryUtils;
import vn.fptu.reasbe.utils.mapper.DesiredItemMapper;
import vn.fptu.reasbe.utils.mapper.ItemMapper;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse.getPageable;

/**
 * @author ntig
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final UserService userService;
    private final AuthService authService;
    private final VectorStoreService vectorStoreService;
    private final ItemRepository itemRepository;
    private final DesiredItemRepository desiredItemRepository;
    private final UserSubscriptionService userSubscriptionService;
    private final ItemMapper itemMapper;
    private final DesiredItemMapper desiredItemMapper;

    @Value("${goongio.config.api-key}")
    private String GOONGIO_API_KEY;

    @Value("${goongio.config.distance-matrix-url}")
    private String GOONGIO_DISTANCE_MATRIX_URL;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public BaseSearchPaginationResponse<SearchItemResponse> searchItemPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchItemRequest request) {
        return BaseSearchPaginationResponse.of(itemRepository.searchItemPagination(request, getPageable(pageNo, pageSize, sortBy, sortDir))
                .map(item -> itemMapper.toSearchItemResponse(item, getFavIds())));
    }

    @Override
    public BaseSearchPaginationResponse<ItemResponse> getAllItemOfUserByStatus(int pageNo, int pageSize, String sortBy, String sortDir, Integer userId, StatusItem statusItem) {
        if (statusItem.equals(StatusItem.AVAILABLE) || statusItem.equals(StatusItem.SOLD)) {
            return BaseSearchPaginationResponse.of(getAllItemByUserIdAndStatusItem(userId, statusItem, getPageable(pageNo, pageSize, sortBy, sortDir)).map(this::mapToItemResponsesWithFavorite));
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.invalidStatusItem");
        }
    }

    @Override
    public BaseSearchPaginationResponse<ItemResponse> getAllItemOfCurrentUserByStatusItem(int pageNo, int pageSize, String sortBy, String sortDir, StatusItem statusItem) {
        User user = authService.getCurrentUser();
        return BaseSearchPaginationResponse.of(getAllItemByUserIdAndStatusItem(user.getId(), statusItem, getPageable(pageNo, pageSize, sortBy, sortDir)).map(itemMapper::toItemResponse));
    }

    @Override
    public Item getItemById(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
    }

    @Override
    public ItemResponse getItemDetail(Integer id) {
        Item item = getItemById(id);
        return mapToItemResponsesWithFavorite(item);
    }

    @Override
    public Item uploadItem(UploadItemRequest request) {
        User currentUser = authService.getCurrentUser();

        validateMaxItemUploadedInCurrentMonth(currentUser);

        Item newItem = itemMapper.toEntity(request);
        newItem.setCategory(categoryService.getCategoryById(request.getCategoryId()));
        newItem.setBrand(brandService.getBrandById(request.getBrandId()));
        newItem.setOwner(currentUser);
        newItem.setStatusItem(StatusItem.PENDING);
        newItem.setUserLocation(userService.getPrimaryUserLocation(currentUser));

        if (request.getDesiredItem() != null) {
            newItem.setTypeExchange(TypeExchange.EXCHANGE_WITH_DESIRED_ITEM);
            DesiredItem newDesiredItem = desiredItemMapper.toDesiredItem(request.getDesiredItem());
            prepareDesiredItem(newDesiredItem, request.getDesiredItem());
            newItem.setDesiredItem(desiredItemRepository.save(newDesiredItem));
        } else {
            newItem.setTypeExchange(TypeExchange.OPEN_EXCHANGE);
        }
        return itemRepository.save(newItem);
    }

    @Override
    public ItemResponse updateItem(UpdateItemRequest request) {
        Item existedItem = getItemById(request.getId());
        User currentUser = authService.getCurrentUser();
        if (!existedItem.getOwner().equals(currentUser)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.invalidOwner");
        }

        if (!existedItem.getStatusItem().equals(StatusItem.PENDING) && !existedItem.getStatusItem().equals(StatusItem.AVAILABLE)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.cannotUpdateItem");
        }

        itemMapper.updateItem(existedItem, request);

        existedItem.setStatusItem(StatusItem.PENDING);

        DesiredItem existedDesiredItem = existedItem.getDesiredItem();
        DesiredItemDto desiredItemDto = request.getDesiredItem();

        if (desiredItemDto == null) {
            // If there's an existing item, remove it
            if (existedDesiredItem != null) {
                existedItem.setTypeExchange(TypeExchange.OPEN_EXCHANGE);
                desiredItemRepository.delete(existedDesiredItem);
                existedItem.setDesiredItem(null);
            }
        } else {
            // desiredItemDto is not null
            validateDesiredItem(desiredItemDto);

            if (existedDesiredItem != null) {
                // Update existing desired item
                desiredItemMapper.updateDesiredItem(existedDesiredItem, desiredItemDto);
                prepareDesiredItem(existedDesiredItem, desiredItemDto);
                desiredItemRepository.save(existedDesiredItem);
            } else {
                // Create new desired item
                existedItem.setTypeExchange(TypeExchange.EXCHANGE_WITH_DESIRED_ITEM);
                DesiredItem newDesiredItem = desiredItemMapper.toDesiredItem(desiredItemDto);
                prepareDesiredItem(newDesiredItem, desiredItemDto);
                existedItem.setDesiredItem(desiredItemRepository.save(newDesiredItem));
            }
        }
        vectorStoreService.deleteItem(List.of(existedItem));

        return itemMapper.toItemResponse(itemRepository.save(existedItem));
    }

    @Override
    public BaseSearchPaginationResponse<ItemResponse> getAllPendingItem(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return BaseSearchPaginationResponse.of(itemRepository.findAllByStatusItem(StatusItem.PENDING, pageable).map(itemMapper::toItemResponse));
    }

    @Override
    public ItemResponse reviewItem(Integer id, StatusItem status) {
        Item pendingItem = getItemById(id);

        if (!pendingItem.getStatusItem().equals(StatusItem.PENDING))
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.pendingItemOnly");

        if (status.equals(StatusItem.AVAILABLE)) {
            pendingItem.setStatusItem(StatusItem.AVAILABLE);
            pendingItem.setApprovedTime(DateUtils.getCurrentDateTime().toLocalDate().atStartOfDay());

            int expiredTime = userSubscriptionService.checkIfUserPremium(pendingItem.getOwner()) ? AppConstants.EXPIRED_TIME_WEEKS_PREMIUM : AppConstants.EXPIRED_TIME_WEEKS;
            pendingItem.setExpiredTime(pendingItem.getApprovedTime().plusWeeks(expiredTime));

            vectorStoreService.addNewItem(List.of(pendingItem));

        } else if (status.equals(StatusItem.REJECTED)) {
            pendingItem.setStatusItem(StatusItem.REJECTED);
        }

        return itemMapper.toItemResponse(itemRepository.save(pendingItem));
    }

    @Override
    public List<ItemResponse> getRecommendedItems(Integer itemId, int limit) {
        User curr = authService.getCurrentUser();

        Item item = getItemById(itemId);

        if (!item.getOwner().equals(curr)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.invalidOwner");
        }

        DesiredItem desiredItem = item.getDesiredItem();

        if (desiredItem == null) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.desiredItemEmpty");
        }

        String filter = buildFilterForRecommendedItem(desiredItem);

        List<Document> documents = vectorStoreService.searchSimilarItems(desiredItem.getDescription(), filter, limit);

        return mapToItemResponses(documents);
    }

    @Override
    public List<ItemResponse> getRecommendedItemsInExchange(Integer itemId, int limit) {
        User currBuyer = authService.getCurrentUser();

        Item sellerItem = getItemById(itemId);

        DesiredItem desiredItem = sellerItem.getDesiredItem();

        if (desiredItem == null) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.desiredItemEmpty");
        }

        String filter = buildFilterForRecommendedItemInExchange(desiredItem, currBuyer.getId());

        List<Document> documents = vectorStoreService.searchSimilarItems(desiredItem.getDescription(), filter, limit);

        return mapToItemResponses(documents);
    }

    @Override
    public List<ItemResponse> getSimilarItems(Integer itemId, int limit) {
        Item item = getItemById(itemId);

        String itemContent = String.format("Item: %s, Brand: %s, Category: %s, Price: %s, Description: %s, Condition: %s",
                item.getItemName(),
                item.getBrand().getBrandName(),
                item.getCategory().getCategoryName(),
                item.getPrice().toString(),
                item.getDescription(),
                item.getConditionItem().getCode());

        String filter = "itemId != " + item.getId();

        List<Document> documents = vectorStoreService.searchSimilarItems(itemContent, filter, limit);

        return mapToItemResponses(documents);
    }

    @Override
    public List<ItemResponse> getOtherItemsOfUser(Integer currItemId, Integer userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return itemRepository.findByStatusItemAndOwnerIdAndIdNotOrderByApprovedTimeDesc(StatusItem.AVAILABLE, userId, currItemId, pageable)
                .stream()
                .map(itemMapper::toItemResponse)
                .toList();
    }

    @Override
    public ItemResponse changeItemStatus(Integer itemId, StatusItem status) {
        User user = authService.getCurrentUser();

        Item item = getItemById(itemId);

        if (!item.getOwner().equals(user)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.invalidOwner");
        }

        item.setStatusItem(status);

        return itemMapper.toItemResponse(itemRepository.save(item));
    }

    @Override
    public boolean isItemExistedAndExpired(Integer itemId) {
        return itemRepository.existsByIdAndStatusItemEqualsAndStatusEntityEquals(itemId, StatusItem.EXPIRED, StatusEntity.ACTIVE);
    }

    @Override
    public void extendItem(Item item, SubscriptionPlan plan) {
        item.setStatusItem(StatusItem.AVAILABLE);
        item.setExpiredTime(DateUtils.getCurrentDateTime().plusSeconds((long) (plan.getDuration() * 24 * 60 * 60)));
        itemRepository.save(item);
    }

    @Override
    public BaseSearchPaginationResponse<ItemResponse> findNearbyItems(int pageNo, int pageSize, double latitude, double longitude, double distance) {
        Point refPoint = GeometryUtils.createPoint(longitude, latitude);
        double distanceInMeters = distance * 1000;

        List<Item> items = itemRepository.findNearbyItems(refPoint, distanceInMeters, StatusItem.AVAILABLE.getCode());

        DistanceMatrixResponse response = getDistanceMatrix(latitude, longitude, items);

        // Kiểm tra nếu response không có hàng hoặc không có elements, trả về danh sách rỗng
        if (response.getRows().isEmpty()) {
            return BaseSearchPaginationResponse.of(Page.empty());
        }

        List<DistanceMatrixResponse.Element> elements = response.getRows().getFirst().getElements();

        // Kiểm tra nếu số lượng elements không khớp với số lượng items
        if (elements.size() != items.size()) {
            return BaseSearchPaginationResponse.of(Page.empty());
        }

        // Ánh xạ từng Item với kết quả tương ứng từ API và giữ đúng thứ tự với LinkedHashMap
        Map<Item, DistanceMatrixResponse.Element> itemDistanceMap = new LinkedHashMap<>();
        for (int i = 0; i < items.size(); i++) {
            itemDistanceMap.put(items.get(i), elements.get(i));
        }

        // Lọc các item theo khoảng cách mong muốn
        List<ItemResponse> filteredItems = itemDistanceMap.entrySet().stream()
                .filter(entry -> entry.getValue().getDistance().getValue() <= distanceInMeters)
                .map(entry -> {
                    ItemResponse itemResponse = itemMapper.toItemResponse(entry.getKey());
                    itemResponse.setDistance(entry.getValue().getDistance().getText());
                    return itemResponse;
                })
                .toList();

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), filteredItems.size());
        List<ItemResponse> pagedList = filteredItems.subList(startIndex, endIndex);
        return BaseSearchPaginationResponse.of(new PageImpl<>(pagedList, pageable, filteredItems.size()));
    }

    public DistanceMatrixResponse getDistanceMatrix(double originLat, double originLng, List<Item> items) {
        // Build the list of destinations
        String destinations = items.stream()
                .map(item -> item.getUserLocation().getLatitude() + "," + item.getUserLocation().getLongitude())
                .collect(Collectors.joining("|"));

        // Build the URL with parameters
        String url = GOONGIO_DISTANCE_MATRIX_URL +
                "?origins=" + originLat + "," + originLng +
                "&destinations=" + destinations +
                "&vehicle=" + "car" +
                "&api_key=" + GOONGIO_API_KEY;

        //Calling API
        DistanceMatrixResponse response = restTemplate.getForObject(url, DistanceMatrixResponse.class);
        if (response == null) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.distanceMatrixAPIResponseNull");
        }

        return response;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void checkExpiredItems() {
        List<Item> expiredItems = itemRepository.findAllByExpiredTimeBeforeAndStatusItem(DateUtils.getCurrentDateTime(), StatusItem.AVAILABLE);
        expiredItems.forEach(expiredItem -> expiredItem.setStatusItem(StatusItem.EXPIRED));
        itemRepository.saveAll(expiredItems);
        log.info("Updated {} expired items", expiredItems.size());
    }

    private void validateMaxItemUploadedInCurrentMonth(User currentUser) {
        LocalDateTime firstDayOfMonth = LocalDateTime.of(DateUtils.getCurrentYear(), Month.of(DateUtils.getCurrentMonth()), 1, 0, 0, 0);
        LocalDateTime lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        int countItem = itemRepository.countByOwnerAndStatusItemInAndCreationDateBetween(
                currentUser, List.of(StatusItem.UNAVAILABLE, StatusItem.PENDING, StatusItem.AVAILABLE), firstDayOfMonth, lastDayOfMonth);

        int maximumItem = userSubscriptionService.checkIfUserPremium(currentUser) ? AppConstants.MAX_ITEM_UPLOADED_PREMIUM : AppConstants.MAX_ITEM_UPLOADED;

        if (countItem >= maximumItem) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.itemUploadAtMax");
        }
    }

    private Page<Item> getAllItemByUserIdAndStatusItem(Integer userId, StatusItem statusItem, Pageable pageable) {
        return itemRepository.findAllByOwnerIdAndStatusItemOrderByCreationDateDesc(userId, statusItem, pageable);
    }

    private void prepareDesiredItem(DesiredItem desiredItem, DesiredItemDto desiredItemDto) {
        desiredItem.setBrand(brandService.getBrandById(desiredItemDto.getBrandId()));
        desiredItem.setCategory(categoryService.getCategoryById(desiredItemDto.getCategoryId()));
    }

    private void validateDesiredItem(DesiredItemDto dto) {
        if (dto.getMinPrice().compareTo(dto.getMaxPrice()) > 0) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.minPriceGreaterThanMaxPrice");
        }
    }

    private ItemResponse mapToItemResponsesWithFavorite(Item item) {
        return itemMapper.toItemResponseWithFavorite(item, getFavIds());
    }

    private List<Integer> getFavIds() {
        User user = authService.getCurrentUser();

        List<Integer> favIds = new ArrayList<>();

        if (user != null && user.getFavorites() != null && !user.getFavorites().isEmpty()) {
            favIds = user.getFavorites().stream()
                    .map(favorite -> favorite.getItem().getId())  // Extract item ID
                    .toList();
        }

        return favIds;
    }

    private List<ItemResponse> mapToItemResponses(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            return new ArrayList<>();
        }

        // Dùng Set để loại bỏ ID trùng lặp
        Set<Integer> itemIds = documents.stream()
                .map(doc -> Integer.valueOf(doc.getMetadata().get("itemId").toString()))
                .collect(Collectors.toCollection(LinkedHashSet::new)); // Bảo toàn thứ tự

        // Lấy tất cả items từ DB
        Map<Integer, ItemResponse> itemResponseMap = itemRepository.findAllById(itemIds).stream()
                .collect(Collectors.toMap(Item::getId, this::mapToItemResponsesWithFavorite, (a, b) -> a, LinkedHashMap::new));

        // Đảm bảo trả về theo đúng thứ tự itemIds ban đầu
        return itemIds.stream()
                .map(itemResponseMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    private String buildFilterForRecommendedItemInExchange(DesiredItem desiredItem, Integer buyerId) {
        StringBuilder filter = new StringBuilder("ownerId == " + buyerId);

        return getCommonItemFilter(desiredItem, filter);
    }

    private String buildFilterForRecommendedItem(DesiredItem desiredItem) {
        StringBuilder filter = new StringBuilder("ownerId != " + desiredItem.getItem().getOwner().getId());

        return getCommonItemFilter(desiredItem, filter);
    }

    private String getCommonItemFilter(DesiredItem desiredItem, StringBuilder filter) {
        if (desiredItem.getBrand() != null) {
            filter.append(" && brandName == '").append(desiredItem.getBrand().getBrandName()).append("'");
        }
        if (desiredItem.getCategory() != null) {
            filter.append(" && categoryName == '").append(desiredItem.getCategory().getCategoryName()).append("'");
        }
        if (desiredItem.getConditionItem() != null) {
            filter.append(" && conditionItem == ").append(desiredItem.getConditionItem().getCode());
        }
        if (desiredItem.getMinPrice() != null) {
            filter.append(" && price >= ").append(desiredItem.getMinPrice());
        }
        if (desiredItem.getMaxPrice() != null) {
            filter.append(" && price <= ").append(desiredItem.getMaxPrice());
        }

        return filter.toString();
    }
}

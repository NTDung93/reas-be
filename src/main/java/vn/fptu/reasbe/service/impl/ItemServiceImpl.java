package vn.fptu.reasbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.desireditem.DesiredItemDto;
import vn.fptu.reasbe.model.dto.item.ItemResponse;
import vn.fptu.reasbe.model.dto.item.SearchItemRequest;
import vn.fptu.reasbe.model.dto.item.SearchItemResponse;
import vn.fptu.reasbe.model.dto.item.UpdateItemRequest;
import vn.fptu.reasbe.model.dto.item.UploadItemRequest;
import vn.fptu.reasbe.model.entity.DesiredItem;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.DesiredItemRepository;
import vn.fptu.reasbe.repository.ItemRepository;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.BrandService;
import vn.fptu.reasbe.service.CategoryService;
import vn.fptu.reasbe.service.ItemService;
import vn.fptu.reasbe.service.UserService;
import vn.fptu.reasbe.utils.common.DateUtils;
import vn.fptu.reasbe.utils.mapper.DesiredItemMapper;
import vn.fptu.reasbe.utils.mapper.ItemMapper;

import java.util.List;

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
    private final ItemRepository itemRepository;
    private final DesiredItemRepository desiredItemRepository;
    private final ItemMapper itemMapper;
    private final DesiredItemMapper desiredItemMapper;

    @Override
    public BaseSearchPaginationResponse<SearchItemResponse> searchItemPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchItemRequest request) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return BaseSearchPaginationResponse.of(itemRepository.searchItemPagination(request, pageable).map(itemMapper::toSearchItemResponse));
    }

    @Override
    public List<ItemResponse> getAllItemOfUser(Integer userId, StatusItem statusItem) {
        if (statusItem.equals(StatusItem.AVAILABLE) || statusItem.equals(StatusItem.NO_LONGER_FOR_EXCHANGE)) {
            return getAllItemByUserIdAndStatusItem(userId, statusItem);
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.invalidStatusItem");
        }
    }

    @Override
    public List<ItemResponse> getAllItemOfCurrentUserByStatusItem(StatusItem statusItem) {
        User user = authService.getCurrentUser();
        return getAllItemByUserIdAndStatusItem(user.getId(), statusItem);
    }

    @Override
    public Item getItemById(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
    }

    @Override
    public Item uploadItem(UploadItemRequest request) {
        User currentUser = authService.getCurrentUser();

        Item newItem = itemMapper.toEntity(request);
        newItem.setCategory(categoryService.getCategoryById(request.getCategoryId()));
        newItem.setBrand(brandService.getBrandById(request.getBrandId()));
        newItem.setOwner(currentUser);
        newItem.setStatusItem(StatusItem.PENDING);
        newItem.setUserLocation(userService.getPrimaryUserLocation(currentUser));

        if (request.getDesiredItem() != null) {
            DesiredItem newDesiredItem = desiredItemMapper.toDesiredItem(request.getDesiredItem());
            prepareDesiredItem(newDesiredItem, request.getDesiredItem());
            newItem.setDesiredItem(desiredItemRepository.save(newDesiredItem));
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

        itemMapper.updateItem(existedItem, request);

        DesiredItem existedDesiredItem = existedItem.getDesiredItem();
        DesiredItemDto desiredItemDto = request.getDesiredItem();

        if (desiredItemDto == null) {
            // If there's an existing item, remove it
            if (existedDesiredItem != null) {
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
                DesiredItem newDesiredItem = desiredItemMapper.toDesiredItem(desiredItemDto);
                prepareDesiredItem(newDesiredItem, desiredItemDto);
                existedItem.setDesiredItem(desiredItemRepository.save(newDesiredItem));
            }
        }

        return itemMapper.toItemResponse(itemRepository.save(existedItem));
    }

    @Override
    public List<ItemResponse> getAllPendingItem() {
        return itemRepository.findAllByStatusItem(StatusItem.PENDING)
                .stream()
                .map(itemMapper::toItemResponse)
                .toList();
    }

    @Override
    public ItemResponse reviewItem(Integer id, StatusItem status) {
        Item pendingItem = getItemById(id);

        if (!pendingItem.getStatusItem().equals(StatusItem.PENDING))
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.pendingItemOnly");

        if (status.equals(StatusItem.AVAILABLE)) {
            pendingItem.setStatusItem(StatusItem.AVAILABLE);
            pendingItem.setApprovedTime(DateUtils.getCurrentDateTime().toLocalDate().atStartOfDay());
            pendingItem.setExpiredTime(pendingItem.getApprovedTime().plusWeeks(AppConstants.EXPIRED_TIME_WEEKS));
        } else if (status.equals(StatusItem.REJECTED)) {
            pendingItem.setStatusItem(StatusItem.REJECTED);
        }

        return itemMapper.toItemResponse(itemRepository.save(pendingItem));
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void checkExpiredItems(){
        //TODO: in testing
        List<Item> expiredItems = itemRepository.findAllByExpiredTimeBeforeAndStatusItem(DateUtils.getCurrentDateTime(), StatusItem.AVAILABLE);
        expiredItems.forEach(expiredItem -> expiredItem.setStatusItem(StatusItem.EXPIRED));
        itemRepository.saveAll(expiredItems);
        log.info("Updated {} expired items", expiredItems.size());
    }

    private List<ItemResponse> getAllItemByUserIdAndStatusItem(Integer userId, StatusItem statusItem) {
        return itemRepository.findAllByOwnerIdAndStatusItemOrderByCreationDateDesc(userId, statusItem)
                .stream()
                .map(itemMapper::toItemResponse)
                .toList();
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
}

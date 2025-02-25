package vn.fptu.reasbe.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
import vn.fptu.reasbe.model.entity.UserLocation;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.DesiredItemRepository;
import vn.fptu.reasbe.repository.ItemRepository;
import vn.fptu.reasbe.repository.UserRepository;
import vn.fptu.reasbe.service.BrandService;
import vn.fptu.reasbe.service.CategoryService;
import vn.fptu.reasbe.service.ItemService;
import vn.fptu.reasbe.utils.mapper.DesiredItemMapper;
import vn.fptu.reasbe.utils.mapper.ItemMapper;

import java.time.LocalDateTime;

/**
 * @author ntig
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
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
    public ItemResponse uploadItem(UploadItemRequest request) {
        User currentUser = getCurrentUser();

        Item newItem = itemMapper.toEntity(request);
        newItem.setCategory(categoryService.getCategoryById(request.getCategoryId()));
        newItem.setBrand(brandService.getBrandById(request.getBrandId()));
        newItem.setOwner(currentUser);
        newItem.setStatusItem(StatusItem.PENDING);
        newItem.setUserLocation(getPrimaryUserLocation(currentUser));

        if (request.getDesiredItem() != null) {
            DesiredItem newDesiredItem = desiredItemMapper.toDesiredItem(request.getDesiredItem());
            newDesiredItem.setCategory(categoryService.getCategoryById(request.getDesiredItem().getCategoryId()));
            newDesiredItem.setBrand(brandService.getBrandById(request.getDesiredItem().getBrandId()));
            newItem.setDesiredItem(desiredItemRepository.save(newDesiredItem));
        }

        return itemMapper.toItemResponse(itemRepository.save(newItem));
    }

    @Override
    public ItemResponse getItemDetail(Integer id) {
        return itemMapper.toItemResponse(getItemById(id));
    }

    @Override
    public ItemResponse updateItem(UpdateItemRequest request) {
        Item existedItem = getItemById(request.getId());
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
                updateDesiredItem(existedDesiredItem, desiredItemDto);
                desiredItemRepository.save(existedDesiredItem);
            } else {
                // Create new desired item
                DesiredItem newDesiredItem = desiredItemMapper.toDesiredItem(desiredItemDto);
                updateDesiredItem(newDesiredItem, desiredItemDto);
                existedItem.setDesiredItem(desiredItemRepository.save(newDesiredItem));
            }
        }


        return itemMapper.toItemResponse(itemRepository.save(existedItem));
    }

    @Override
    public ItemResponse reviewItem(Integer id, StatusItem status) {
        Item pendingItem = getItemById(id);

        if (!pendingItem.getStatusItem().equals(StatusItem.PENDING))
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Only item with PENDING status allows to be reviewed.");

        if (status.equals(StatusItem.APPROVED)) {
            pendingItem.setStatusItem(StatusItem.APPROVED);
            pendingItem.setExpiredTime(LocalDateTime.now().plusWeeks(AppConstants.EXPIRED_TIME_WEEKS));
        } else if (status.equals(StatusItem.REJECTED)) {
            pendingItem.setStatusItem(StatusItem.REJECTED);
        }

        return itemMapper.toItemResponse(itemRepository.save(pendingItem));
    }

    private void updateDesiredItem(DesiredItem desiredItem, DesiredItemDto desiredItemDto) {
        desiredItem.setBrand(brandService.getBrandById(desiredItemDto.getBrandId()));
        desiredItem.setCategory(categoryService.getCategoryById(desiredItemDto.getCategoryId()));
    }

    private void validateDesiredItem(DesiredItemDto dto) {
        if (dto.getMinPrice().compareTo(dto.getMaxPrice()) > 0) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Min price must not be greater than max price.");
        }
    }

    private Item getItemById(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
    }

    private UserLocation getPrimaryUserLocation(User user) {
        return user.getUserLocations()
                .stream()
                .filter(UserLocation::isPrimary)
                .findFirst()
                .orElse(null);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}

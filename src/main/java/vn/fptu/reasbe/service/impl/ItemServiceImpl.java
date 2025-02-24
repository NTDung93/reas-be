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
import vn.fptu.reasbe.model.dto.item.*;
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
 *
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

        if (existedDesiredItem != null && desiredItemDto == null) {
            // Case 1: Remove the existing desired item
            desiredItemRepository.delete(existedDesiredItem);
            existedItem.setDesiredItem(null);
        } else if (existedDesiredItem != null && desiredItemDto != null) {
            // Case 2: Update the existing desired item
            desiredItemMapper.updateDesiredItem(existedDesiredItem, desiredItemDto);
            desiredItemRepository.save(existedDesiredItem);
        } else if (existedDesiredItem == null && desiredItemDto != null) {
            // Case 3: Add a new desired item
            DesiredItem newDesiredItem = desiredItemMapper.toDesiredItem(desiredItemDto);
            existedItem.setDesiredItem(desiredItemRepository.save(newDesiredItem));
        }

        return itemMapper.toItemResponse(itemRepository.save(existedItem));
    }

    @Override
    public ItemResponse reviewItem(Integer id, StatusItem status) {
        Item pendingItem = getItemById(id);

        if (status.equals(StatusItem.APPROVED)) {
            pendingItem.setStatusItem(StatusItem.APPROVED);
            pendingItem.setExpiredTime(LocalDateTime.now().plusWeeks(AppConstants.EXPIRED_TIME_WEEKS));
        } else if (status.equals(StatusItem.REJECTED)) {
            pendingItem.setStatusItem(StatusItem.REJECTED);
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Invalid status for reviewing item");
        }

        return itemMapper.toItemResponse(itemRepository.save(pendingItem));
    }

    private Item getItemById(Integer id){
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

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}

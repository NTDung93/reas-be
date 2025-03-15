package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;

import vn.fptu.reasbe.model.dto.item.ItemResponse;
import vn.fptu.reasbe.model.dto.item.SearchItemRequest;
import vn.fptu.reasbe.model.dto.item.SearchItemResponse;
import vn.fptu.reasbe.model.dto.item.UpdateItemRequest;
import vn.fptu.reasbe.model.dto.item.UploadItemRequest;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.enums.item.StatusItem;

import java.util.List;

/**
 * @author ntig
 */
public interface ItemService {
    BaseSearchPaginationResponse<SearchItemResponse> searchItemPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchItemRequest request);

    Item getItemById(Integer id);

    Item uploadItem(UploadItemRequest request);

    List<ItemResponse> getAllItemOfUser(Integer userId, StatusItem statusItem);

    List<ItemResponse> getAllItemOfCurrentUserByStatusItem(StatusItem statusItem);

    ItemResponse updateItem(UpdateItemRequest request);

    List<ItemResponse> getAllPendingItem();

    ItemResponse reviewItem(Integer id, StatusItem status);
}

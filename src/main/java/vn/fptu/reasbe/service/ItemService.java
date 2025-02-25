package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;

import vn.fptu.reasbe.model.dto.item.ItemResponse;
import vn.fptu.reasbe.model.dto.item.SearchItemRequest;
import vn.fptu.reasbe.model.dto.item.SearchItemResponse;
import vn.fptu.reasbe.model.dto.item.UpdateItemRequest;
import vn.fptu.reasbe.model.dto.item.UploadItemRequest;
import vn.fptu.reasbe.model.enums.item.StatusItem;

/**
 * @author ntig
 */
public interface ItemService {
    BaseSearchPaginationResponse<SearchItemResponse> searchItemPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchItemRequest request);

    ItemResponse uploadItem(UploadItemRequest request);

    ItemResponse getItemDetail(Integer id);

    ItemResponse updateItem(UpdateItemRequest request);

    ItemResponse reviewItem(Integer id, StatusItem status);
}

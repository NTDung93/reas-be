package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.product.SearchProductRequest;
import vn.fptu.reasbe.model.dto.product.SearchProductResponse;

public interface ProductService {
    BaseSearchPaginationResponse<SearchProductResponse> searchProductPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchProductRequest request);
}

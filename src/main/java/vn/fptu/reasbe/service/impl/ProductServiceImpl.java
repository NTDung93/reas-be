package vn.fptu.reasbe.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.product.SearchProductRequest;
import vn.fptu.reasbe.model.dto.product.SearchProductResponse;
import vn.fptu.reasbe.repository.ProductRepository;
import vn.fptu.reasbe.service.ProductService;
import vn.fptu.reasbe.utils.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public BaseSearchPaginationResponse<SearchProductResponse> searchProductPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchProductRequest request) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<SearchProductResponse> people = productRepository.searchProductPagination(request, pageable).map(productMapper::toSearchProductResponse);
        return BaseSearchPaginationResponse.of(people);
    }
}

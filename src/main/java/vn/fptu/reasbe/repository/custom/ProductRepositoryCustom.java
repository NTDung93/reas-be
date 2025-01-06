package vn.fptu.reasbe.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.fptu.reasbe.model.dto.product.SearchProductRequest;
import vn.fptu.reasbe.model.entity.Product;

/**
 *
 * @author ntig
 */
public interface ProductRepositoryCustom {
    Page<Product> searchProductPagination(SearchProductRequest request, Pageable pageable);
}

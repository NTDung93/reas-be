package vn.fptu.reasbe.model.dto.product;

import java.math.BigDecimal;

import vn.fptu.reasbe.model.dto.brand.BrandDto;
import vn.fptu.reasbe.model.dto.category.CategoryDto;
import vn.fptu.reasbe.model.enums.EntityStatus;
import vn.fptu.reasbe.model.enums.item.StatusItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductResponse {
    private String productName;
    private String description;
    private BigDecimal price;
    private String image;
    private StatusItem productStatus;
    private EntityStatus entityStatus;
    private CategoryDto category;
    private BrandDto brand;
}

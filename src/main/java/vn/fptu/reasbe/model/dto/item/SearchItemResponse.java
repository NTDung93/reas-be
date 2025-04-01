package vn.fptu.reasbe.model.dto.item;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.dto.brand.BrandDto;
import vn.fptu.reasbe.model.dto.category.CategoryDto;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationDto;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.item.StatusItem;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchItemResponse {
    Integer id;
    String itemName;
    String description;
    BigDecimal price;
    String imageUrl;
    StatusItem statusItem;
    StatusEntity statusEntity;
    CategoryDto category;
    BrandDto brand;
    UserResponse owner;
    UserLocationDto userLocation;
    boolean isFavorite;
}

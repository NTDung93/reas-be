package vn.fptu.reasbe.model.dto.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.dto.brand.BrandDto;
import vn.fptu.reasbe.model.dto.category.CategoryDto;
import vn.fptu.reasbe.model.dto.desireditem.DesiredItemResponse;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationDto;
import vn.fptu.reasbe.model.enums.item.StatusItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemResponse {
    Integer id;
    String itemName;
    String description;
    BigDecimal price;
    String imageUrl;
    StatusItem statusItem;
    Boolean moneyAccepted;
    String termsAndConditionsExchange;
    LocalDateTime expiredTime;
    CategoryDto category;
    BrandDto brand;
    UserResponse owner;
    DesiredItemResponse desiredItem;
    UserLocationDto userLocation;
}

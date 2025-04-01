package vn.fptu.reasbe.model.dto.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.dto.brand.BrandDto;
import vn.fptu.reasbe.model.dto.category.CategoryDto;
import vn.fptu.reasbe.model.dto.desireditem.DesiredItemResponse;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationDto;
import vn.fptu.reasbe.model.enums.item.ConditionItem;
import vn.fptu.reasbe.model.enums.item.MethodExchange;
import vn.fptu.reasbe.model.enums.item.StatusItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    Boolean moneyAccepted;
    StatusItem statusItem;
    ConditionItem conditionItem;
    String termsAndConditionsExchange;
    LocalDateTime expiredTime;
    LocalDateTime approvedTime;
    List<MethodExchange> methodExchanges;
    CategoryDto category;
    BrandDto brand;
    UserResponse owner;
    DesiredItemResponse desiredItem;
    UserLocationDto userLocation;
    boolean isFavorite;
}

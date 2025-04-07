package vn.fptu.reasbe.model.dto.desireditem;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.category.TypeItem;
import vn.fptu.reasbe.model.enums.item.ConditionItem;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DesiredItemResponse {
    Integer id;

    Integer categoryId;

    String categoryName;

    Integer brandId;

    String brandName;

    TypeItem typeItem;

    ConditionItem conditionItem;

    BigDecimal minPrice;

    BigDecimal maxPrice;

    String description;
}

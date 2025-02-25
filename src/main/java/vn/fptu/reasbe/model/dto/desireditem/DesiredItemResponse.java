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

    TypeItem typeItem;

    String categoryName;

    ConditionItem conditionItem;

    String brandName;

    BigDecimal minPrice;

    BigDecimal maxPrice;
}

package vn.fptu.reasbe.model.dto.desireditem;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.item.ConditionItem;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DesiredItemResponse {
    Integer id;

    String categoryName;

    String brandName;

    ConditionItem conditionItem;

    BigDecimal minPrice;

    BigDecimal maxPrice;

    String description;
}

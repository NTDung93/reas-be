package vn.fptu.reasbe.model.dto.desireditem;

import jakarta.validation.constraints.NotNull;
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
public class DesiredItemDto {

    @NotNull(message = "Type of item cannot be blank")
    TypeItem typeItem;

    @NotNull(message = "Category cannot be blank")
    Integer categoryId;

    @NotNull(message = "Condition cannot be blank")
    ConditionItem conditionItem;

    @NotNull(message = "Brand cannot be blank")
    Integer brandId;

    @NotNull(message = "Min price cannot be blank")
    BigDecimal minPrice;

    @NotNull(message = "Max price cannot be blank")
    BigDecimal maxPrice;
}

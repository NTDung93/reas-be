package vn.fptu.reasbe.model.dto.desireditem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class DesiredItemDto {

    @NotNull(message = "Category cannot be blank")
    Integer categoryId;

    @NotNull(message = "Condition cannot be blank")
    ConditionItem conditionItem;

    @NotNull(message = "Brand cannot be blank")
    Integer brandId;

    @NotNull(message = "Min price cannot be blank")
    @Min(value = 0, message = "Min price must be greater than or equal to 0")
    BigDecimal minPrice;

    @NotNull(message = "Max price cannot be blank")
    BigDecimal maxPrice;
}

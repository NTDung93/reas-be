package vn.fptu.reasbe.model.dto.desireditem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    Integer categoryId;

    ConditionItem conditionItem;

    Integer brandId;

    @NotNull(message = "Min price must not be blank")
    @Min(value = 0, message = "Min price must be greater than or equal to 0")
    BigDecimal minPrice;

    BigDecimal maxPrice;

    @NotBlank(message = "Description must not be blank")
    String description;
}

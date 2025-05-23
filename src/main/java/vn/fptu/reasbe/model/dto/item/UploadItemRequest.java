package vn.fptu.reasbe.model.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.dto.desireditem.DesiredItemDto;
import vn.fptu.reasbe.model.enums.item.ConditionItem;
import vn.fptu.reasbe.model.enums.item.MethodExchange;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadItemRequest {
    @NotBlank(message = "Item's name cannot be blank")
    String itemName;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 20, message = "Description must have at least 20 characters")
    String description;

    BigDecimal price;

    @NotNull(message = "Condition cannot be blank")
    ConditionItem conditionItem;

    @NotBlank(message = "Image cannot be blank")
    String imageUrl;

    @NotEmpty(message = "Method of exchange cannot be empty")
    @Size(min = 1, message = "Method of exchanges must have at least 1 method")
    List<MethodExchange> methodExchanges;

    @NotNull(message = "Exchanging with money option cannot be null")
    Boolean isMoneyAccepted;

    String termsAndConditionsExchange;

    @NotNull(message = "Category cannot be blank")
    Integer categoryId;

    @NotNull(message = "Brand cannot be blank")
    Integer brandId;

    @NotNull(message = "User location cannot be blank")
    Integer userLocationId;

    DesiredItemDto desiredItem;
}

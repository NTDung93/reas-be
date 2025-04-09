package vn.fptu.reasbe.model.dto.item;

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
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRunnerDTO {
    Integer itemId;
    String itemName;
    String brandName;
    String categoryName;
    BigDecimal price;
    String description;
    ConditionItem conditionItem;
    Integer ownerId;
}

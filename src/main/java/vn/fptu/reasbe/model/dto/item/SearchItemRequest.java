package vn.fptu.reasbe.model.dto.item;

import java.math.BigDecimal;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.item.StatusItem;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchItemRequest {
    String itemName;
    String description;
    BigDecimal price;
    BigDecimal fromPrice;
    BigDecimal toPrice;
    String imageUrl;
    List<Integer> categoryIds;
    List<Integer> brandIds;
    List<Integer> ownerIds;
    List<Integer> locationIds;
    List<StatusItem> statusItems;
    List<StatusEntity> statusEntities;
}

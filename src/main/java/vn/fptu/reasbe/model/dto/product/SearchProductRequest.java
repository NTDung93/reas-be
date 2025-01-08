package vn.fptu.reasbe.model.dto.product;

import java.math.BigDecimal;
import java.util.List;

import vn.fptu.reasbe.model.enums.EntityStatus;
import vn.fptu.reasbe.model.enums.item.StatusItem;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchProductRequest {
    String productName;
    String description;
    BigDecimal price;
    BigDecimal fromPrice;
    BigDecimal toPrice;
    String image;
    List<Integer> categoryIds;
    List<Integer> brandIds;
    List<StatusItem> productStatuses;
    List<EntityStatus> entityStatuses;
}

package vn.fptu.reasbe.model.dto.subscriptionplan;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.subscriptionplan.TypeSubscriptionPlan;

/**
 *
 * @author dungnguyen
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionPlanDto {
    Integer id;

    @NotBlank(message = "Subscription plan's name cannot be blank")
    String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, message = "Description must have at least 10 characters")
    String description;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0)
    BigDecimal price;

    @NotBlank(message = "Image url cannot be blank")
    String imageUrl;

    @NotNull(message = "Type of subscription plan cannot be null")
    TypeSubscriptionPlan typeSubscriptionPlan;

    @NotNull(message = "Duration cannot be null")
    @Min(value = 0)
    Float duration; // count by month
}

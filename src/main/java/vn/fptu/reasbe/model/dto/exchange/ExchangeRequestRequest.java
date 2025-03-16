package vn.fptu.reasbe.model.dto.exchange;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.item.MethodExchange;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeRequestRequest {

    @NotNull(message = "Seller item cannot be blank")
    Integer sellerItemId;

    Integer buyerItemId;

    @NotNull(message = "Paid by user cannot be blank")
    Integer paidByUserId;

    @NotNull(message = "Exchange date cannot be blank")
    LocalDateTime exchangeDate;

    @NotBlank(message = "Exchange location cannot be blank")
    String exchangeLocation;

    @NotNull(message = "Estimate price cannot be blank")
    @Min(value = 0, message = "Price must be greater than or equal 0")
    BigDecimal estimatePrice;

    @NotNull(message = "Method of exchange cannot be blank")
    MethodExchange methodExchange;

    String additionalNotes;
}

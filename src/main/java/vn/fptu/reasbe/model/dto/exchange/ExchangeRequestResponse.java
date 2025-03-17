package vn.fptu.reasbe.model.dto.exchange;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.dto.item.ItemResponse;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeRequest;
import vn.fptu.reasbe.model.enums.item.MethodExchange;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeRequestResponse {
    Integer id;

    ItemResponse sellerItem;

    ItemResponse buyerItem;

    UserResponse paidBy;

    LocalDateTime exchangeDate;

    String exchangeLocation;

    BigDecimal estimatePrice;

    BigDecimal finalPrice;

    Integer numberOfOffer;

    MethodExchange methodExchange;

    StatusExchangeRequest statusExchangeRequest;

    Boolean buyerConfirmation;

    Boolean sellerConfirmation;

    String additionalNotes;
}

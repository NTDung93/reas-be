package vn.fptu.reasbe.model.dto.exchange;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeHistoryResponse {

    Integer id;

    Boolean buyerConfirmation;

    Boolean sellerConfirmation;

    String buyerImageUrl;

    String sellerImageUrl;

    String buyerAdditionalNotes;

    String sellerAdditionalNotes;

    StatusExchangeHistory statusExchangeHistory;
}

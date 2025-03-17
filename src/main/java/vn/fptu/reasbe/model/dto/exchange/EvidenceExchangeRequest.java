package vn.fptu.reasbe.model.dto.exchange;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvidenceExchangeRequest {

    @NotNull(message = "ExchangeHistoryId must not be null")
    Integer exchangeHistoryId;

    @NotNull(message = "Image must not be blank")
    String imageUrl;

    String additionalNotes;
}

package vn.fptu.reasbe.model.dto.feedback;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackRequest {
    Integer id;

    @NotNull(message = "Item must not be blank")
    Integer itemId;

    @NotNull(message = "Exchange History must not be blank")
    Integer exchangeHistoryId;

    @NotNull(message = "Rating must not be blank")
    @Range(min = 1, max = 5, message = "Rating must be in range 1 and 5")
    Integer rating;

    String comment;

    String imageUrl;
}

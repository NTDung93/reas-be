package vn.fptu.reasbe.model.dto.payos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentLinkRequest {

    @NotBlank(message = "Description cannot be blank!")
    @Size(min = 1, max = 20, message = "Description must be between 1 and 20 characters!")
    private String description;

    private Integer subscriptionPlanId;

    private Integer itemId;

    @NotBlank(message = "Return Url cannot be blank!")
    private String returnUrl;

    @NotBlank(message = "Cancel Url cannot be blank!")
    private String cancelUrl;
}

package vn.fptu.reasbe.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fptu.reasbe.model.constant.AppConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleSignUpDto {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @Pattern(regexp = AppConstants.EMAIL_REGEX, message = "Email is invalid!")
    private String email;

    @NotBlank(message = "FullName cannot be blank")
    private String fullName;

    @NotBlank(message = "GoogleId cannot be blank")
    private String googleId;

    private String photoUrl;
}

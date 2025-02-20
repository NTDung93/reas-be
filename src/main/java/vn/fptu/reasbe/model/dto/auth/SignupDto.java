package vn.fptu.reasbe.model.dto.auth;

import vn.fptu.reasbe.model.constant.AppConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @Pattern(regexp = AppConstants.EMAIL_REGEX, message = "Email is invalid!")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = AppConstants.PASSWORD_REGEX, message = "Password must be at least 8 characters with at least one uppercase letter, one number, and one special character (!@#$%^&*).")
    private String password;

    @NotBlank(message = "FullName cannot be blank")
    private String fullName;
}

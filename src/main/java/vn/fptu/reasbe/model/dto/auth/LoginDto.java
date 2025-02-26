package vn.fptu.reasbe.model.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotEmpty(message = "Email cannot be blank")
    private String userNameOrEmailOrPhone;

    @NotEmpty(message = "Password cannot be blank")
    private String password;
}

package vn.fptu.reasbe.model.dto.auth;

import java.util.List;

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

    private List<String> registrationTokens;

    public LoginDto(String userNameOrEmailOrPhone, String password) {
        this.userNameOrEmailOrPhone = userNameOrEmailOrPhone;
        this.password = password;
    }
}

package vn.fptu.reasbe.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.enums.user.Gender;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateResidentRequest {
    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, message = "Full name must have at least 2 characters")
    String fullName;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = AppConstants.PHONE_REGEX, message = "Invalid phone number!")
    String phone;

    @NotNull(message = "Gender cannot be blank")
    Gender gender;

    String image;

    @NotNull(message = "Primary location cannot be null")
    Integer userLocationId;
}
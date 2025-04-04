package vn.fptu.reasbe.model.dto.userlocation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLocationRequest {
    Integer id;

    @NotBlank(message = "Specific address must not be blank")
    String specificAddress;

    @NotNull(message = "Latitude must not be null")
    Double latitude;

    @NotNull(message = "Longitude must not be null")
    Double longitude;

    @NotNull(message = "Location must not be null")
    Integer locationId;
}

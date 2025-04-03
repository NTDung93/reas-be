package vn.fptu.reasbe.model.dto.userlocation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.dto.location.LocationDto;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLocationDto {
    Integer id;
    Integer userId;
    String specificAddress;
    double latitude;
    double longitude;
    boolean isPrimary;
    LocationDto location;
}

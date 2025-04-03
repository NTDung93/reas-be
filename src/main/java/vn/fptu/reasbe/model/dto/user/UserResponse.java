package vn.fptu.reasbe.model.dto.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationDto;
import vn.fptu.reasbe.model.enums.core.StatusEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fptu.reasbe.model.enums.user.Gender;
import vn.fptu.reasbe.model.enums.user.RoleName;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Integer id;
    String userName;
    String fullName;
    String email;
    String phone;
    Gender gender;
    Integer numOfExchangedItems;
    Integer numOfFeedbacks;
    Double numOfRatings;
    StatusEntity statusEntity;
    String image;
    RoleName roleName;
    LocalDateTime creationDate;
    List<UserLocationDto> userLocations;
    boolean isFirstLogin;
}

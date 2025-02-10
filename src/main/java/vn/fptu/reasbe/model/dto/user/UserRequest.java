package vn.fptu.reasbe.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fptu.reasbe.model.enums.user.Gender;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Integer id;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private Gender gender;
    private String image;
}

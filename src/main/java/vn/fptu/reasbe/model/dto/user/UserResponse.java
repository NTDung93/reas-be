package vn.fptu.reasbe.model.dto.user;

import vn.fptu.reasbe.model.enums.core.StatusEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private StatusEntity status;
    private int point;
    private boolean isFirstLogin;
    private String image;
    private String roleName;
}

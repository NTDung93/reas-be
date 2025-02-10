package vn.fptu.reasbe.model.dto.user;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.user.Gender;
import vn.fptu.reasbe.model.enums.user.RoleName;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchUserRequest {
    String userName;
    String fullName;
    String email;
    String phone;
    List<Gender> genders;
    List<StatusEntity> statusEntities;
    List<RoleName> roleNames;
}

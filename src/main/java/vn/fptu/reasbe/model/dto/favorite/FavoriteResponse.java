package vn.fptu.reasbe.model.dto.favorite;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.dto.item.ItemResponse;
import vn.fptu.reasbe.model.dto.user.UserResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteResponse {
    ItemResponse item;
    UserResponse user;
}

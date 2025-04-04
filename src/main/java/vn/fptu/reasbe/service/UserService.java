package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.user.CreateStaffRequest;
import vn.fptu.reasbe.model.dto.user.SearchUserRequest;
import vn.fptu.reasbe.model.dto.user.UpdateResidentRequest;
import vn.fptu.reasbe.model.dto.user.UpdateStaffRequest;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.entity.UserLocation;

/**
 * @author ntig
 */
public interface UserService {
    BaseSearchPaginationResponse<UserResponse> searchUserPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchUserRequest request);

    UserResponse createNewStaff(CreateStaffRequest request);

    UserResponse updateStaff(UpdateStaffRequest request);

    Boolean deactivateStaff(Integer userId);

    User getUserById(Integer id);

    UserLocation getPrimaryUserLocation(User user);

    UserResponse loadDetailInfoUser(Integer userId);

    UserResponse updateResidentInfo(UpdateResidentRequest request);
}

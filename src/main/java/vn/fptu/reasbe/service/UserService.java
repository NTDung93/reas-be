package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.user.CreateStaffRequest;
import vn.fptu.reasbe.model.dto.user.SearchUserRequest;
import vn.fptu.reasbe.model.dto.user.UpdateStaffRequest;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.entity.User;

/**
 * @author ntig
 */
public interface UserService {
    BaseSearchPaginationResponse<UserResponse> searchUserPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchUserRequest request);

    UserResponse createNewStaff(CreateStaffRequest request);

    UserResponse updateStaff(UpdateStaffRequest request);

    Boolean deactivateStaff(Integer userId);

    User getUserById(Integer id);
}

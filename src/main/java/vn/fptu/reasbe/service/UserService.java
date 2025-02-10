package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.user.CreateStaffRequest;
import vn.fptu.reasbe.model.dto.user.SearchUserRequest;
import vn.fptu.reasbe.model.dto.user.UserResponse;

/**
 *
 * @author ntig
 */
public interface UserService {
    BaseSearchPaginationResponse<UserResponse> searchUserPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchUserRequest request);
    UserResponse createNewStaff(CreateStaffRequest request);
}

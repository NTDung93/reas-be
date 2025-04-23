package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationDto;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationRequest;
import vn.fptu.reasbe.model.entity.UserLocation;

public interface UserLocationService {

    BaseSearchPaginationResponse<UserLocationDto> getAllUserLocationOfCurrentUser(int pageNo, int pageSize, String sortBy, String sortDir);

    UserLocation getUserLocationOfCurrentUserById(Integer id);

    UserLocationDto createNewUserLocation(UserLocationRequest request);

    UserLocationDto updateUserLocationOfCurrentUser(UserLocationRequest request);

    Boolean deleteUserLocationOfCurrentUser(Integer id);
}

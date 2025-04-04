package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.location.LocationDto;
import vn.fptu.reasbe.model.entity.Location;

public interface LocationService {
    BaseSearchPaginationResponse<LocationDto> getAllLocation(int pageNo, int pageSize, String sortDir,String sortBy);

    Location getById(Integer id);
}

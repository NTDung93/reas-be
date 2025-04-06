package vn.fptu.reasbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.location.LocationDto;
import vn.fptu.reasbe.model.entity.Location;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.LocationRepository;
import vn.fptu.reasbe.service.LocationService;
import vn.fptu.reasbe.utils.mapper.LocationMapper;

import static vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse.getPageable;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public BaseSearchPaginationResponse<LocationDto> getAllLocation(int pageNo, int pageSize, String sortBy, String sortDir) {
        return BaseSearchPaginationResponse.of(locationRepository.findAllByStatusEntity(StatusEntity.ACTIVE, getPageable(pageNo, pageSize, sortBy, sortDir))
                .map(locationMapper::toDto));
    }

    @Override
    public Location getById(Integer id) {
        return locationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location", "id", id));
    }
}

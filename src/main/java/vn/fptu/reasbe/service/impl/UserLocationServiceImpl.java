package vn.fptu.reasbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationDto;
import vn.fptu.reasbe.model.dto.userlocation.UserLocationRequest;
import vn.fptu.reasbe.model.entity.Location;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.entity.UserLocation;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.UserLocationRepository;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.LocationService;
import vn.fptu.reasbe.service.UserLocationService;
import vn.fptu.reasbe.utils.mapper.UserLocationMapper;

import java.util.Objects;

import static vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse.getPageable;

@Service
@RequiredArgsConstructor
public class UserLocationServiceImpl implements UserLocationService {

    private final AuthService authService;
    private final UserLocationRepository userLocationRepository;
    private final LocationService locationService;
    private final UserLocationMapper userLocationMapper;

    @Override
    public BaseSearchPaginationResponse<UserLocationDto> getAllUserLocationOfCurrentUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = authService.getCurrentUser();

        return BaseSearchPaginationResponse.of(userLocationRepository.findAllByUserAndStatusEntity(user, StatusEntity.ACTIVE, getPageable(pageNo, pageSize, sortBy, sortDir))
                .map(userLocationMapper::toDto));
    }

    @Override
    public UserLocationDto getUserLocationDetailOfCurrentUser(Integer id) {
        User user = authService.getCurrentUser();
        return userLocationMapper.toDto(getUserLocationById(id, user));
    }

    @Override
    public UserLocationDto createNewUserLocation(UserLocationRequest request) {
        User user = authService.getCurrentUser();

        Location location = locationService.getById(request.getLocationId());

        UserLocation userLocation = userLocationMapper.toEntity(request);
        userLocation.setUser(user);
        userLocation.setLocation(location);

        if (request.isPrimary()) {
            updatePrimaryUserLocation(user, userLocation);
        } else {
            boolean hasPrimary = userLocationRepository.existsByIsPrimaryTrueAndUserAndStatusEntity(user, StatusEntity.ACTIVE);
            userLocation.setPrimary(!hasPrimary); // Nếu chưa có thì gán cái này làm primary
        }

        return userLocationMapper.toDto(userLocationRepository.save(userLocation));
    }

    @Override
    public UserLocationDto updateUserLocationOfCurrentUser(UserLocationRequest request) {
        User user = authService.getCurrentUser();

        UserLocation existedUserLocation = getUserLocationById(request.getId(), user);

        validateValidUser(user, existedUserLocation.getUser());

        userLocationMapper.updateEntity(existedUserLocation, request);

        if (!Objects.equals(existedUserLocation.getLocation().getId(), request.getLocationId())) {
            Location location = locationService.getById(request.getLocationId());
            existedUserLocation.setLocation(location);
        }

        if (request.isPrimary()) {
            updatePrimaryUserLocation(user, existedUserLocation);
        }

        return userLocationMapper.toDto(userLocationRepository.save(existedUserLocation));
    }

    @Override
    public Boolean deleteUserLocationOfCurrentUser(Integer id) {
        User user = authService.getCurrentUser();

        UserLocation existedUserLocation = getUserLocationById(id, user);
        validateValidUser(user, existedUserLocation.getUser());

        if (existedUserLocation.isPrimary()) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.cannotDeletePrimaryLocation");
        }

        userLocationRepository.delete(existedUserLocation);
        return true;
    }

    private void updatePrimaryUserLocation(User user, UserLocation newPrimaryLocation) {
        userLocationRepository.findByIsPrimaryTrueAndUserAndStatusEntity(user, StatusEntity.ACTIVE)
                .ifPresent(currentPrimary -> {
                    if (!currentPrimary.getId().equals(newPrimaryLocation.getId())) {
                        currentPrimary.setPrimary(false);
                        userLocationRepository.save(currentPrimary);
                    }
                });

        newPrimaryLocation.setPrimary(true);
    }

    private void validateValidUser(User user1, User user2) {
        if (!Objects.equals(user1.getId(), user2.getId())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.invalidUser");
        }
    }

    private UserLocation getUserLocationById(Integer id, User user) {
        return userLocationRepository.findByIdAndUserAndStatusEntity(id, user, StatusEntity.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("User location", "id", id));
    }
}

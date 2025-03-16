package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.entity.UserLocation;

import java.util.Optional;

public interface UserLocationRepository extends JpaRepository<UserLocation, Integer> {
    Optional<UserLocation> findByIsPrimaryTrueAndUser(User user);
}

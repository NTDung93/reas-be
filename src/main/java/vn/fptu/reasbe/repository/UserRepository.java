package vn.fptu.reasbe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.core.StatusEntity;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserNameOrEmailOrPhone(String email, String userName, String phone);
    List<User> findByRoleNameContainsIgnoreCase(String name);
    Boolean existsByUserName(String userName);
    Boolean existsByEmail(String email);
    Optional<User> findByUserName(String username);
    List<User> findAllByRoleName(String roleName);
    int countUsersByStatusEntityEqualsAndRoleNameEquals(StatusEntity status, String roleName);
}

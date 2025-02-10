package vn.fptu.reasbe.repository;

import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.repository.custom.UserRepositoryCustom;

public interface UserRepository extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User>, UserRepositoryCustom {
    Optional<User> findByUserNameOrEmailOrPhone(String email, String userName, String phone);
    List<User> findByRoleName(String name);
    Boolean existsByUserName(String userName);
    Boolean existsByEmail(String email);
    Optional<User> findByUserName(String username);
    List<User> findAllByRoleName(String roleName);
    int countUsersByStatusEntityEqualsAndRoleNameEquals(StatusEntity status, String roleName);

    Optional<User> findByEmail(@NotNull String email);
}

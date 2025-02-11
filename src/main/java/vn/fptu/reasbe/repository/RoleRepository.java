package vn.fptu.reasbe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.fptu.reasbe.model.entity.Role;
import vn.fptu.reasbe.model.enums.user.RoleName;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}

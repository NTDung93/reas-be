package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fptu.reasbe.model.entity.Brand;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Optional<Brand> getBrandById(Integer id);
}

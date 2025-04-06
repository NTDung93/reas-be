package vn.fptu.reasbe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.fptu.reasbe.model.entity.Location;
import vn.fptu.reasbe.model.enums.core.StatusEntity;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Page<Location> findAllByStatusEntity(StatusEntity statusEntity, Pageable pageable);
}

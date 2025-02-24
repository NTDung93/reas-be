package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fptu.reasbe.model.entity.DesiredItem;

public interface DesiredItemRepository extends JpaRepository<DesiredItem, Integer> {
}

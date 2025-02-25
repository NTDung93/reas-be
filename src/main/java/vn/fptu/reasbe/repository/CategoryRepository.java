package vn.fptu.reasbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fptu.reasbe.model.entity.Category;
import vn.fptu.reasbe.model.enums.category.TypeItem;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> getCategoryById(Integer id);

    List<Category> findAllByTypeItem(TypeItem typeItem);
}

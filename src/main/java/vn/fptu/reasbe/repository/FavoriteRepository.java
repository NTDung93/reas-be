package vn.fptu.reasbe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.fptu.reasbe.model.entity.Favorite;
import vn.fptu.reasbe.model.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Page<Favorite> findAllByUser(User user, Pageable pageable);
}

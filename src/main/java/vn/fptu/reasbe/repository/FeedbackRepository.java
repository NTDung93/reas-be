package vn.fptu.reasbe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.fptu.reasbe.model.entity.Feedback;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.core.StatusEntity;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Page<Feedback> getAllByItemOwnerAndStatusEntity(User user, StatusEntity statusEntity, Pageable pageable);
    Page<Feedback> getAllByItemOwnerAndRatingAndStatusEntity(User user, Integer rating, StatusEntity statusEntity, Pageable pageable);
    Optional<Feedback> findByIdAndStatusEntity(Integer id, StatusEntity statusEntity);
}

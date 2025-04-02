package vn.fptu.reasbe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.fptu.reasbe.model.entity.Feedback;
import vn.fptu.reasbe.model.entity.User;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Page<Feedback> getAllByItemOwner(User user, Pageable pageable);
    Page<Feedback> getAllByItemOwnerAndRating(User user, Integer rating, Pageable pageable);
}

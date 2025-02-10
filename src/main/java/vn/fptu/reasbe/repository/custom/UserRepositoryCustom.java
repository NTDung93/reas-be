package vn.fptu.reasbe.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.fptu.reasbe.model.dto.user.SearchUserRequest;
import vn.fptu.reasbe.model.entity.User;

/**
 *
 * @author ntig
 */
public interface UserRepositoryCustom {
    Page<User> searchUserPagination(SearchUserRequest request, Pageable pageable);
}

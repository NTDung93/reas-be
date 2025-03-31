package vn.fptu.reasbe.repository.mongodb;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import vn.fptu.reasbe.model.enums.user.StatusOnline;
import vn.fptu.reasbe.model.mongodb.User;

/**
 *
 * @author dungnguyen
 */
public interface UserMRepository extends MongoRepository<User, String> {
    List<User> findAllByStatusOnline(StatusOnline statusOnline);

    Optional<User> findUserByRefIdIs(Integer refId);

    Optional<User> findUserByUserName(String userName);
}

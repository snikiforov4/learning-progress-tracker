package tracker.repository;

import tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    User save(User user);

    Optional<User> findById(String id);

    List<User> getAll();

    boolean existsByEmail(String email);

}

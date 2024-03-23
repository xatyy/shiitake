package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foodx.backend.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findOneById(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}

package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.model.user.UserRole;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

    User findOneById(UUID id);


    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}

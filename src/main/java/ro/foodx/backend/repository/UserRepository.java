package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.model.user.UserRole;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User>, PagingAndSortingRepository<User, UUID> {
    User findByUsername(String username);

    User findOneById(UUID id);


    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}

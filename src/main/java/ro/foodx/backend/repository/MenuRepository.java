package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foodx.backend.model.general.Menu;
import ro.foodx.backend.model.user.UserRole;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, UserRole> {
   List<Menu> findByUserRole(UserRole userRole);
}

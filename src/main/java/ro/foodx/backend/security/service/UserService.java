package ro.foodx.backend.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.model.user.UserRole;
import ro.foodx.backend.security.dto.AuthenticatedUserDto;
import ro.foodx.backend.security.dto.RegistrationRequest;
import ro.foodx.backend.security.dto.RegistrationResponse;

import javax.management.relation.Role;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User findByUsername(String username);

    UserRole getRoleByToken(String token);
    User getUserByToken(String token);


    RegistrationResponse registration(RegistrationRequest registrationRequest);

    AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

    Page<User> getUsersAsOwner(String token, Pageable pageable);
    Page<User> getUsersFilteredAsOwner(String token, Map<String, String> filters, Pageable pageable);


}

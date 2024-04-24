package ro.foodx.backend.security.service;

import ro.foodx.backend.model.user.User;
import ro.foodx.backend.model.user.UserRole;
import ro.foodx.backend.security.dto.AuthenticatedUserDto;
import ro.foodx.backend.security.dto.RegistrationRequest;
import ro.foodx.backend.security.dto.RegistrationResponse;

import javax.management.relation.Role;
import java.util.Optional;

public interface UserService {
    User findByUsername(String username);

    UserRole getRoleByToken(String token);


    RegistrationResponse registration(RegistrationRequest registrationRequest);

    AuthenticatedUserDto findAuthenticatedUserByUsername(String username);


}

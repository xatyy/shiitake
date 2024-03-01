package ro.foodx.backend.security.service;

import ro.foodx.backend.model.user.User;
import ro.foodx.backend.security.dto.AuthenticatedUserDto;
import ro.foodx.backend.security.dto.RegistrationRequest;
import ro.foodx.backend.security.dto.RegistrationResponse;

public interface UserService {
    User findByUsername(String username);

    RegistrationResponse registration(RegistrationRequest registrationRequest);

    AuthenticatedUserDto findAuthenticatedUserByUsername(String username);


}

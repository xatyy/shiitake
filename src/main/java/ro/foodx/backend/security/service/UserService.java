package ro.foodx.backend.security.service;

import ro.foodx.backend.model.user.User;
import ro.foodx.backend.security.dto.AuthenticatedUserDto;
import ro.foodx.backend.security.dto.RegistrationRequest;
import ro.foodx.backend.security.dto.RegistrationResponse;

public interface UserService {
    User findByEmail(String email);

    RegistrationResponse registration(RegistrationRequest registrationRequest);

    AuthenticatedUserDto findAuthenticatedUserByEmail(String email);


}

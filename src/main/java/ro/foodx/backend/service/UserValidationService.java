package ro.foodx.backend.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.foodx.backend.exceptions.RegistrationException;
import ro.foodx.backend.repository.UserRepository;
import ro.foodx.backend.security.dto.RegistrationRequest;
import ro.foodx.backend.utils.ExceptionMessageAccessor;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {
    private static final String EMAIL_ALREADY_EXISTS = "email_already_exists";

    private static final String USERNAME_ALREADY_EXISTS = "username_already_exists";

    private final UserRepository userRepository;

    private final ExceptionMessageAccessor exceptionMessageAccessor;

    public void validateUser(RegistrationRequest registrationRequest) {

        final String email = registrationRequest.getEmail();

        checkEmail(email);


    }


    private void checkEmail(String email) {

        final boolean existsByEmail = userRepository.existsByEmail(email);

        if (existsByEmail) {

            log.warn("{} is already being used!", email);

            final String existsEmail = exceptionMessageAccessor.getMessage(null, EMAIL_ALREADY_EXISTS);
            throw new RegistrationException(existsEmail);
        }
    }
}

package ro.foodx.backend.security.jwt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ro.foodx.backend.exceptions.LoginException;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.security.dto.AuthenticatedUserDto;
import ro.foodx.backend.security.dto.LoginRequest;
import ro.foodx.backend.security.dto.LoginResponse;
import ro.foodx.backend.security.mapper.UserMapper;
import ro.foodx.backend.security.service.UserService;
import ro.foodx.backend.repository.UserRepository;
import ro.foodx.backend.utils.ExceptionMessageAccessor;
import ro.foodx.backend.utils.GeneralMessageAccessor;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final UserService userService;

    private final JwtTokenManager jwtTokenManager;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final ExceptionMessageAccessor exceptionMessageAccessor;

    private final GeneralMessageAccessor generalMessageAccessor;
    private static final String ACCOUNT_NOT_CONFIRMED = "account_not_confirmed";
    private static final String LOGIN_SUCCESSFUL = "login_successful";
    private static final String CREDENTIALS_ERROR = "credentials_error";

    public LoginResponse getLoginResponse(LoginRequest loginRequest) {
        final String username = loginRequest.getEmail();
        final String password = loginRequest.getPassword();

        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);


        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }
        catch (Exception e){
            log.info("Incorrect email or password.");
            final String credentialsError = exceptionMessageAccessor.getMessage(null, CREDENTIALS_ERROR);
            throw new LoginException(credentialsError);
        }
        final AuthenticatedUserDto authenticatedUserDto = userService.findAuthenticatedUserByUsername(username);






        final User user = UserMapper.INSTANCE.convertToUser(authenticatedUserDto);

        final User userTest = userRepository.findByUsername(user.getEmail());

        System.out.println(userTest.getIsConfirmed());
        if(userTest.getIsConfirmed()) {
            final String token = jwtTokenManager.generateToken(user);

            log.info("{} has successfully logged in!", user.getEmail());

            final String loginSuccessful = generalMessageAccessor.getMessage(null, LOGIN_SUCCESSFUL);
            return new LoginResponse(token, loginSuccessful);
        }else{
            log.info("{} is not confirmed!", user.getEmail());
            final String notConfirmed = exceptionMessageAccessor.getMessage(null, ACCOUNT_NOT_CONFIRMED);
            throw new LoginException(notConfirmed);
        }

    }


}

package ro.foodx.backend.security.jwt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.security.dto.AuthenticatedUserDto;
import ro.foodx.backend.security.dto.LoginRequest;
import ro.foodx.backend.security.dto.LoginResponse;
import ro.foodx.backend.security.mapper.UserMapper;
import ro.foodx.backend.security.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final UserService userService;

    private final JwtTokenManager jwtTokenManager;

    private final AuthenticationManager authenticationManager;

    public LoginResponse getLoginResponse(LoginRequest loginRequest) {
        final String username = loginRequest.getEmail();
        final String password = loginRequest.getPassword();

        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        final AuthenticatedUserDto authenticatedUserDto = userService.findAuthenticatedUserByUsername(username);

        final User user = UserMapper.INSTANCE.convertToUser(authenticatedUserDto);
        final String token = jwtTokenManager.generateToken(user);

        log.info("{} has successfully logged in!", user.getEmail());

        return new LoginResponse(token);
    }


}

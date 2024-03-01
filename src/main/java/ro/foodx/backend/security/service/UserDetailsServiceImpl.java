package ro.foodx.backend.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.foodx.backend.model.user.UserRole;
import ro.foodx.backend.security.dto.AuthenticatedUserDto;

import java.util.Collections;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String EMAIL_OR_PASSWORD_INVALID = "Invalid email or password.";

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        final AuthenticatedUserDto authenticatedUser = userService.findAuthenticatedUserByUsername(username);

        if (Objects.isNull(authenticatedUser)) {
            throw new UsernameNotFoundException(EMAIL_OR_PASSWORD_INVALID);
        }

        final String authenticatedEmail = authenticatedUser.getUsername();
        final String authenticatedPassword = authenticatedUser.getPassword();
        final UserRole userRole = authenticatedUser.getUserRole();
        final SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.name());

        return new User(authenticatedEmail, authenticatedPassword, Collections.singletonList(grantedAuthority));
    }
}

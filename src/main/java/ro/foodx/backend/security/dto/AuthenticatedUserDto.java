package ro.foodx.backend.security.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.foodx.backend.model.user.UserRole;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String password;

    private UserRole userRole;

}

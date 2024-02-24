package ro.foodx.backend.security.mapper;

import org.mapstruct.factory.Mappers;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.security.dto.AuthenticatedUserDto;
import ro.foodx.backend.security.dto.RegistrationRequest;

public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User convertToUser(RegistrationRequest registrationRequest);

    AuthenticatedUserDto convertToAuthenticatedUserDto(User user);

    User convertToUser(AuthenticatedUserDto authenticatedUserDto);
}

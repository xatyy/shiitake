package ro.foodx.backend.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.model.user.Verification;
import ro.foodx.backend.security.dto.AuthenticatedUserDto;
import ro.foodx.backend.security.dto.RegistrationRequest;
import ro.foodx.backend.security.dto.VerificationRequest;

import java.util.Optional;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User convertToUser(RegistrationRequest registrationRequest);

    AuthenticatedUserDto convertToAuthenticatedUserDto(User user);

    User convertToUser(AuthenticatedUserDto authenticatedUserDto);

    Verification convertToVerification(VerificationRequest verificationRequest);
}

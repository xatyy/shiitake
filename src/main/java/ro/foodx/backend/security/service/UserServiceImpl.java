package ro.foodx.backend.security.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.model.user.UserRole;
import ro.foodx.backend.model.user.Verification;
import ro.foodx.backend.repository.StoreSpecifications;
import ro.foodx.backend.repository.UserRepository;
import ro.foodx.backend.repository.UserSpecifications;
import ro.foodx.backend.repository.VerificationRepository;
import ro.foodx.backend.security.dto.AuthenticatedUserDto;
import ro.foodx.backend.security.dto.RegistrationRequest;
import ro.foodx.backend.security.dto.RegistrationResponse;
import ro.foodx.backend.security.mapper.UserMapper;
import ro.foodx.backend.service.UserValidationService;
import ro.foodx.backend.service.email.EmailService;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private static final String REGISTRATION_SUCCESSFUL = "registration_successful";

    private final UserRepository userRepository;

    private final VerificationRepository verificationRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserValidationService userValidationService;

    private final GeneralMessageAccessor generalMessageAccessor;

    private final EmailService emailService;


    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public UserRole getRoleByToken(String token) {
        User user = userValidationService.returnUserFromToken(token);
        return user.getUserRole();
    }

    @Override
    public User getUserByToken(String token) {
        return userValidationService.returnUserFromToken(token);
    }


    @Override
    public RegistrationResponse registration(RegistrationRequest registrationRequest) {

        userValidationService.validateUser(registrationRequest);

        final User user = UserMapper.INSTANCE.convertToUser(registrationRequest);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Long cui = registrationRequest.getCUI();

        if (cui != null) {

            if (String.valueOf(cui).length() > 10 || String.valueOf(cui).length() < 2) {
                user.setUserRole(UserRole.USER);
            }

            long v = 753217532;

            // Extract the control digit
            int c1 = (int) (cui % 10);
            cui /= 10;

            // Execute operations on digits
            long t = 0;
            while (cui > 0) {
                t += (cui % 10) * (v % 10);
                cui /= 10;
                v /= 10;
            }

            // Apply multiplication by 10 and find modulo 11
            int c2 = (int) (t * 10 % 11);

            // If modulo 11 is 10, then the control digit is 0
            if (c2 == 10) {
                c2 = 0;
            }

            if (c1 == c2) {
                user.setUserRole(UserRole.PARTNER);
            } else {
                user.setUserRole(UserRole.USER);
            }
        }
        if(registrationRequest.getCUI() == null){
            user.setUserRole(UserRole.USER);
        }
        user.setIsConfirmed(false);

        userRepository.save(user);

        final Verification verification = new Verification();

        verification.setUser(user);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        verification.setTimestamp(timestamp.getTime());
        Verification newVerification = verificationRepository.save(verification);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("system@foodx.ro");
        simpleMailMessage.setTo();
        String to = user.getEmail();
        String subject = "Activate your account";
        String message = "Activate your account using this link https://cloud.foodx.ro/verify/" + newVerification.getId().toString();
        emailService.sendHtmlMessage(to, subject, message);

        final String username = registrationRequest.getUsername();
        final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, username);

        log.info("{} registered successfully!", username);

        return new RegistrationResponse(registrationSuccessMessage);
    }

    @Override
    public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {

        final User user = findByUsername(username);

        log.info("{} Test passed", user.getId());

        return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
    }

    @Override
    public Page<User> getUsersAsOwner(String token, Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    @Override
    public Page<User> getUsersFilteredAsOwner(String token, Map<String, String> filters, Pageable pageable) {
        Specification<User> spec = UserSpecifications.byFilter(filters);  // Use the combined specification
        return userRepository.findAll(spec, pageable);
    }

}

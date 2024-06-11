package ro.foodx.backend.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import ro.foodx.backend.exceptions.VerificationException;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.model.user.Verification;
import ro.foodx.backend.repository.UserRepository;
import ro.foodx.backend.repository.VerificationRepository;
import ro.foodx.backend.security.dto.VerificationRequest;
import ro.foodx.backend.security.dto.VerificationResponse;
import ro.foodx.backend.service.email.EmailService;
import ro.foodx.backend.utils.ExceptionMessageAccessor;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import java.sql.Timestamp;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService{
    private static final String CONFIRMATION_SUCCESSFUL = "confirmation_successful";

    private static final String NEW_CODE = "new_code";
    private static final String ALREADY_CONFIRMED = "already_confirmed";

    private final GeneralMessageAccessor generalMessageAccessor;

    private final ExceptionMessageAccessor exceptionMessageAccessor;
    private static final String CODE_EXPIRED = "code_expired";

    private final UserRepository userRepository;

    private final VerificationRepository verificationRepository;

    private final EmailService emailService;

    @Override
    public Verification getVerificationById(UUID verificationId) {
        return verificationRepository.findOneById(verificationId);
    }

    @Override
    public VerificationResponse getNewCode(VerificationRequest verificationRequest) {
        String email = verificationRequest.getEmail();
        User user = userRepository.findByUsername(email);
        if(user == null){
            final String userNotFound = exceptionMessageAccessor.getMessage(null, CODE_EXPIRED);
            throw new VerificationException(userNotFound);
        }

        if(user.getIsConfirmed()){
            final String alreadyConfirmed = exceptionMessageAccessor.getMessage(null, ALREADY_CONFIRMED);
            throw new VerificationException(alreadyConfirmed);
        }
        UUID userId = user.getId();
        Verification verification = verificationRepository.findOneByUser_Id(userId);

        if(verification == null) {
            final Verification newVerification = new Verification();
            newVerification.setUser(user);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            newVerification.setTimestamp(timestamp.getTime());
            Verification savedVerification = verificationRepository.save(newVerification);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("system@foodx.ro");
            simpleMailMessage.setTo(user.getEmail());
            simpleMailMessage.setSubject("Activate your account");
            simpleMailMessage.setText("Activate your account using this link https://dashboard.foodx.ro/verify/" + savedVerification.getId().toString());

            String to = user.getEmail();
            String subject = "Activate your account";
            String message = "Activate your account using this link https://cloud.foodx.ro/verify/" + newVerification.getId().toString();
            emailService.sendHtmlMessage(to, subject, message);
        } else {
            verificationRepository.delete(verification);
            final Verification newVerification = new Verification();
            newVerification.setUser(user);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            newVerification.setTimestamp(timestamp.getTime());
            Verification savedVerification = verificationRepository.save(newVerification);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            String to = user.getEmail();
            String subject = "Activate your account";
            String message = "Activate your account using this link https://cloud.foodx.ro/verify/" + newVerification.getId().toString();
            emailService.sendHtmlMessage(to, subject, message);
        }
        final String newCodeMessage = generalMessageAccessor.getMessage(null, NEW_CODE);

        log.info("New code generated successfully!");

        return new VerificationResponse(newCodeMessage);

    }

    @Override
    public VerificationResponse verifyAccount(UUID verificationId) {
            Verification verification = getVerificationById(verificationId);
            if (verification == null) {
                final String codeExpired = exceptionMessageAccessor.getMessage(null, CODE_EXPIRED);
                throw new VerificationException(codeExpired);
            }
            User user = verification.getUser();
            if (user == null) {
                final String codeExpired = exceptionMessageAccessor.getMessage(null, CODE_EXPIRED);
                throw new VerificationException(codeExpired);
            }
            user.setIsConfirmed(true);
            userRepository.save(user);

            final String confirmationSuccessMessage = generalMessageAccessor.getMessage(null, CONFIRMATION_SUCCESSFUL);

            log.info("Confirmed successfully!");

            return new VerificationResponse(confirmationSuccessMessage);


    }
}

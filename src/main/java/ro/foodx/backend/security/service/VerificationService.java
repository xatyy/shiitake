package ro.foodx.backend.security.service;

import ro.foodx.backend.model.user.User;
import ro.foodx.backend.model.user.Verification;
import ro.foodx.backend.security.dto.VerificationRequest;
import ro.foodx.backend.security.dto.VerificationResponse;

import java.util.UUID;

public interface VerificationService {
    Verification getVerificationById(UUID verificationId);
    VerificationResponse verifyAccount(UUID verificationId);

    VerificationResponse getNewCode(VerificationRequest verificationRequest);
}

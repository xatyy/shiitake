package ro.foodx.backend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class VerificationRequest {
    @NotEmpty(message = "{login_email_not_empty}")
    private String email;
}

package ro.foodx.backend.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.foodx.backend.controller.Login;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = Login.class)
public class LoginControllerAdvice {

    @ExceptionHandler(LoginException.class)
    ResponseEntity<ApiExceptionResponse> handleRegistrationException(LoginException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

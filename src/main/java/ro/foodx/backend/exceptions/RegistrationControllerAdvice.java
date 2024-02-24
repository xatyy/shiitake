package ro.foodx.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.foodx.backend.controller.Registration;

import java.time.LocalDateTime;


@RestControllerAdvice(basePackageClasses = Registration.class)
public class RegistrationControllerAdvice {
    @ExceptionHandler(RegistrationException.class)
    ResponseEntity<ApiExceptionResponse> handleRegistrationException(RegistrationException exception) {

        final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

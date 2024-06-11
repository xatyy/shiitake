package ro.foodx.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.foodx.backend.controller.VerificationController;
import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = VerificationController.class)
public class VerificationControllerAdvice {

    @ExceptionHandler(VerificationException.class)
    ResponseEntity<ApiExceptionResponse> handleVerificationException(VerificationException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

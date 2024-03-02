package ro.foodx.backend.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.foodx.backend.controller.StoreController;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = StoreController.class)
public class StoreControllerAdvice {
    @ExceptionHandler(StoreCreationException.class)
    ResponseEntity<ApiExceptionResponse> handleStoreException(StoreCreationException exception) {

        final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

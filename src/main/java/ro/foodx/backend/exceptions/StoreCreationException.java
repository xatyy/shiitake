package ro.foodx.backend.exceptions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StoreCreationException extends RuntimeException {
    private final String errorMessage;
}

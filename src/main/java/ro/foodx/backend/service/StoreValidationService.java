package ro.foodx.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreEditRequest;
import ro.foodx.backend.exceptions.StoreCreationException;
import ro.foodx.backend.exceptions.StoreEditException;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.repository.UserRepository;
import ro.foodx.backend.utils.ExceptionMessageAccessor;
import ro.foodx.backend.security.jwt.JwtTokenManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreValidationService {
    private static final String PARTNER_ALREADY_ASSOCIATED = "partner_already_associated";

    private static final String PARTNER_UNAUTHORIZED = "partner_unauthorized";

    private final StoreRepository storeRepository;

    private final UserRepository userRepository;
    private final JwtTokenManager jwt;

    private final ExceptionMessageAccessor exceptionMessageAccessor;

    public void validateOwner(StoreEditRequest storeEditRequest, Long id, String token) {
        final String email = jwt.getEmailFromToken(token);

        User user = userRepository.findByUsername(email);

        Long userId = user.getId();
        Long sellerID =  storeEditRequest.getSellerId();

        if(userId != sellerID) {
            final String unauthorizedId = exceptionMessageAccessor.getMessage(null, PARTNER_UNAUTHORIZED);
            throw new StoreEditException(unauthorizedId);
        }


    }

    public void validateUser(StoreCreateRequest storeCreateRequest) {

        final Long sellerId = storeCreateRequest.getSellerId();

        checkId(sellerId);
    }


    private void checkId(Long sellerId) {

        final boolean existsById = storeRepository.existsById(sellerId);

        if (existsById) {

            log.warn("{} is already being used!", sellerId);

            final String existsId = exceptionMessageAccessor.getMessage(null, PARTNER_ALREADY_ASSOCIATED);
            throw new StoreCreationException(existsId);

        }
    }
}

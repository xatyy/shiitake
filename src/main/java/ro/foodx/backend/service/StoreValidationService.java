package ro.foodx.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.product.ProductCreateRequest;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreEditRequest;
import ro.foodx.backend.exceptions.StoreCreationException;
import ro.foodx.backend.exceptions.StoreEditException;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.repository.UserRepository;
import ro.foodx.backend.utils.ExceptionMessageAccessor;
import ro.foodx.backend.security.jwt.JwtTokenManager;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

    /*

    public void validateProductCreation(ProductCreateRequest productCreateRequest, Long id, String token){
        String rawToken = token.replace("Bearer ", "");
        final String email = jwt.getEmailFromToken(rawToken);

        User user = userRepository.findByUsername(email);
        Optional<Store> store = storeRepository.findById(id);

    } */

    public void validateOwner(Long id, String token) {
        String rawToken = token.replace("Bearer ", "");


        final String email = jwt.getEmailFromToken(rawToken);
        User user = userRepository.findByUsername(email);
        Optional<Store> store = storeRepository.findById(id);

        UUID userId = user.getId();
        if (store.isPresent()) {
            User storeUser = store.get().getUser();


            UUID sellerID = storeUser.getId();


            if (!Objects.equals(userId, sellerID)) {
                log.warn("SellerID and UserID not equal!");

                final String unauthorizedId = exceptionMessageAccessor.getMessage(null, PARTNER_UNAUTHORIZED);
                throw new StoreEditException(unauthorizedId);
            }
        } else {
            final String unauthorizedId = exceptionMessageAccessor.getMessage(null, PARTNER_UNAUTHORIZED);
            throw new StoreEditException(unauthorizedId);
        }
    }

    public void validateUser(StoreCreateRequest storeCreateRequest, String token) {
        String rawToken = token.replace("Bearer ", "");
        final String email = jwt.getEmailFromToken(rawToken);
        final UUID sellerId = storeCreateRequest.getSellerId();
        checkId(sellerId, email);
    }


    private void checkId(UUID sellerId, String email) {

        final User user = userRepository.findByUsername(email);

        final boolean existsById = storeRepository.existsByUser_Id(sellerId);

        final UUID userId = user.getId();

        if(!Objects.equals(userId, sellerId)){
            log.warn("{} and {} are not the same sellers!", sellerId, userId);

            final String unauthorizedId = exceptionMessageAccessor.getMessage(null, PARTNER_UNAUTHORIZED);
            throw new StoreCreationException(unauthorizedId);
        }

        if (existsById) {

            log.warn("{} is already being used!", sellerId);

            final String existsId = exceptionMessageAccessor.getMessage(null, PARTNER_ALREADY_ASSOCIATED);
            throw new StoreCreationException(existsId);

        }
    }
}

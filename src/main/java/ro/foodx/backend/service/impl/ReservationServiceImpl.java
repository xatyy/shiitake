package ro.foodx.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.product.ProductCreateResponse;
import ro.foodx.backend.dto.product.ProductEditRequest;
import ro.foodx.backend.dto.product.ProductEditResponse;
import ro.foodx.backend.dto.reservation.ReservationCreateRequest;
import ro.foodx.backend.dto.reservation.ReservationCreateResponse;
import ro.foodx.backend.exceptions.RegistrationException;
import ro.foodx.backend.mapper.StoreMapper;
import ro.foodx.backend.model.reservation.Reservation;
import ro.foodx.backend.model.reservation.ReservationStatus;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.repository.ProductRepository;
import ro.foodx.backend.repository.ReservationRepository;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.security.service.UserService;
import ro.foodx.backend.service.ReservationService;
import ro.foodx.backend.service.UserValidationService;
import ro.foodx.backend.utils.ExceptionMessageAccessor;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import java.security.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserValidationService userValidationService;
    private static final String PARTNER_NOT_ALLOWED = "partner_not_allowed";
    private static final String RESERVATION_CREATED = "reservation_created";

    private final ExceptionMessageAccessor exceptionMessageAccessor;

    private final StoreRepository storeRepository;

    private final ReservationRepository reservationRepository;

    private final ProductRepository productRepository;

    private final GeneralMessageAccessor generalMessageAccessor;
    @Override
    public List<Reservation> getReservationsByStoreId(Long sellerId) {
        return null;
    }

    @Override
    public ReservationCreateResponse addReservation(ReservationCreateRequest reservationCreateRequest, Long storeId, String token) {
        if(userValidationService.isPartner(token)) {
            log.warn("Partner account tried ordering.");

            final String existsEmail = exceptionMessageAccessor.getMessage(null, PARTNER_NOT_ALLOWED);
            throw new RegistrationException(existsEmail);
        }

        Instant currentInstant = Instant.now();
        final Store store;
        final Product product;
        final User user;
        final double productPrice;
        final long quantity;
        final double reservationPrice;
        final long productId;

        productId = reservationCreateRequest.getProductId();
        store = storeRepository.findOneById(storeId);
        product = productRepository.findOneById(productId);
        user = userValidationService.returnUserFromToken(token);

        final Reservation reservation = StoreMapper.INSTANCE.convertToReservation(reservationCreateRequest);
        reservation.setStore(store);
        reservation.setProduct(product);
        reservation.setUser(user);
        productPrice = product.getPrice();
        quantity = reservation.getQuantity();
        reservationPrice = quantity * productPrice;
        reservation.setReservationPrice(reservationPrice);
        reservation.setReservationStatus(ReservationStatus.PENDING);
        reservation.setReservationTimestamp(String.valueOf(currentInstant));

        //TODO: Implement logic for time limit
        reservation.setPickUpTimeLimit(String.valueOf(currentInstant.plusSeconds(3600)));

        reservationRepository.save(reservation);

        final String reservationSuccessMessage = generalMessageAccessor.getMessage(null, RESERVATION_CREATED);

        log.info("Reservation created successfully!");

        return new ReservationCreateResponse(reservationSuccessMessage);

    }

    @Override
    public ProductEditResponse editProduct(ProductEditRequest productEditRequest, Long storeId, Long productId, String token) {
        return null;
    }
}

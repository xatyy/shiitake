package ro.foodx.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.reservation.ReservationCreateRequest;
import ro.foodx.backend.dto.reservation.ReservationCreateResponse;
import ro.foodx.backend.dto.reservation.ReservationEditRequest;
import ro.foodx.backend.dto.reservation.ReservationEditResponse;
import ro.foodx.backend.exceptions.RegistrationException;
import ro.foodx.backend.mapper.StoreMapper;
import ro.foodx.backend.model.reservation.Reservation;
import ro.foodx.backend.model.reservation.ReservationStatus;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.repository.*;
import ro.foodx.backend.service.ReservationService;
import ro.foodx.backend.service.UserValidationService;
import ro.foodx.backend.utils.ExceptionMessageAccessor;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserValidationService userValidationService;
    private static final String PARTNER_NOT_ALLOWED = "partner_not_allowed";
    private static final String RESERVATION_CREATED = "reservation_created";
    private static final String RESERVATION_MODIFIED_SUCCESSFUL = "reservation_modified";

    private final ExceptionMessageAccessor exceptionMessageAccessor;

    private final StoreRepository storeRepository;

    private final ReservationRepository reservationRepository;

    private final ProductRepository productRepository;

    private final GeneralMessageAccessor generalMessageAccessor;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<Reservation> getReservationsByStoreId(String token) {
        User user = userValidationService.returnUserFromToken(token);
        UUID userId = user.getId();  // Assuming you're using UUID for user IDs
        Store store = storeRepository.findOneByUser_Id(userId);  // Ensure this method correctly fetches the store
        Long storeId = store.getId();

        return reservationRepository.findByStore_Id(storeId);
    }

    @Override
    public Page<Reservation> getReservationsAsOwner(String token, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Reservation> getReservationsFilteredAsOwner(String token, Map<String, String> filters, Pageable pageable) {
        User user = userValidationService.returnUserFromToken(token);
        UUID userId = user.getId();  // Assuming you're using UUID for user IDs
        Store store = storeRepository.findOneByUser_Id(userId);  // Ensure this method correctly fetches the store
        Long storeId = store.getId();

        Specification<Reservation> spec = ReservationSpecifications.byFilter(storeId, filters);  // Use the combined specification
        return reservationRepository.findAll(spec, pageable);
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
        productPrice = product.getFinalPrice();
        quantity = reservation.getQuantity();
        reservationPrice = quantity * productPrice;
        reservation.setReservationPrice(reservationPrice);
        reservation.setReservationStatus(ReservationStatus.PENDING);
        java.sql.Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        reservation.setReservationTimestamp(String.valueOf(timestamp.getTime()));
        String reservationPin = generateRandomPin();
        reservation.setReservationPin(reservationPin);

        //TODO: Implement logic for time limit
        reservation.setPickUpTimeLimit(String.valueOf(currentInstant.plusSeconds(3600)));

        Reservation savedReservation = reservationRepository.save(reservation);

        final String reservationSuccessMessage = generalMessageAccessor.getMessage(null, RESERVATION_CREATED);

        log.info("Reservation created successfully!");

        notifyStoreOfNewOrder(savedReservation);

        return new ReservationCreateResponse(reservationSuccessMessage);

    }

    private void notifyStoreOfNewOrder(Reservation reservation) {
        messagingTemplate.convertAndSend("/topic/newReservation/" + reservation.getStore().getId(), reservation);
    }

    @Transactional
    public ReservationEditResponse editReservation(ReservationEditRequest reservationEditRequest, Long storeId, UUID reservationId, String token) {
        if (userValidationService.isUser(token)) {
            log.warn("Regular account cannot edit reservation.");

            final String existsEmail = exceptionMessageAccessor.getMessage(null, PARTNER_NOT_ALLOWED);
            throw new RegistrationException(existsEmail);
        }

        Optional<Reservation> reservationToEdit = reservationRepository.findById(reservationId);
        if (reservationToEdit.isEmpty()) {
            throw new EntityNotFoundException("Reservation not found");
        }

        Reservation reservation = reservationToEdit.get();

        ReservationStatus newReservationStatus = reservationEditRequest.getReservationStatus();
        if (newReservationStatus != null) {
            reservation.setReservationStatus(newReservationStatus);
        }

        // Save the updated reservation
        reservationRepository.save(reservation);

        final String reservationSuccessMessage = generalMessageAccessor.getMessage(null, RESERVATION_MODIFIED_SUCCESSFUL);
        log.info("Reservation modified successfully!");

        return new ReservationEditResponse(reservationSuccessMessage);
    }

    public long getReservationsForToday(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        LocalDate today = LocalDate.now();
        long startOfDay = today.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        long endOfDay = today.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        return reservationRepository.countReservationsForDay(store, startOfDay, endOfDay);
    }

    public long getReservationsForCurrentMonth(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        YearMonth currentMonth = YearMonth.now();
        long startOfMonth = currentMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        long endOfMonth = currentMonth.atEndOfMonth().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        return reservationRepository.countReservationsForMonth(store, startOfMonth, endOfMonth);
    }

    public long getTotalReservationsSinceRegistration(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        return reservationRepository.countReservationsSinceRegistration(store);
    }


    public long getConfirmedOrPendingReservationsForToday(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        LocalDate today = LocalDate.now();
        long startOfDay = today.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        long endOfDay = today.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        List<ReservationStatusGet> reservationStatus = Arrays.asList(ReservationStatusGet.CONFIRMED, ReservationStatusGet.PENDING);
        return reservationRepository.countReservationsByStatusForDay(store, reservationStatus, startOfDay, endOfDay);
    }

    private String generateRandomPin() {
        final String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder pin = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            pin.append(alphanumeric.charAt(random.nextInt(alphanumeric.length())));
        }

        return pin.toString();
    }
}

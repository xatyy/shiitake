package ro.foodx.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.dashboard.StoreResponse;
import ro.foodx.backend.exceptions.RegistrationException;
import ro.foodx.backend.model.reservation.Reservation;
import ro.foodx.backend.model.store.Contract;
import ro.foodx.backend.model.store.DenyReason;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.repository.ContractRepository;
import ro.foodx.backend.repository.ProductRepository;
import ro.foodx.backend.repository.ReservationRepository;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.service.DashboardService;
import ro.foodx.backend.service.UserValidationService;
import ro.foodx.backend.service.contract.ContractService;
import ro.foodx.backend.service.email.EmailService;
import ro.foodx.backend.utils.ExceptionMessageAccessor;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final UserValidationService userValidationService;
    private static final String NON_ADMIN_DETECTED = "user_not_allowed";
    private static final String STORE_CONFIRMED = "store_confirmed";

    private final ExceptionMessageAccessor exceptionMessageAccessor;
    private final GeneralMessageAccessor generalMessageAccessor;
    private final StoreRepository storeRepository;
    private final ContractRepository contractRepository;
    private final ContractService contractService;
    private final EmailService emailService;
    private final ProductRepository productRepository;
    private final ReservationRepository reservationRepository;

    public List<Reservation> getLastWeekReservations(Long storeId, String token) {
        if (!userValidationService.isAdmin(token)) {
            log.warn("Non-admin tried viewing.");

            final String existsEmail = exceptionMessageAccessor.getMessage(null, NON_ADMIN_DETECTED);
            throw new RegistrationException(existsEmail);
        }

        Store store = storeRepository.findOneById(storeId);
        Long targetId = store.getId();

        // Calculate the start and end of the previous week (Monday to Sunday)
        LocalDate today = LocalDate.now();
        LocalDate lastMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        LocalDate lastSunday = lastMonday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        long startOfWeekMillis = lastMonday.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endOfWeekMillis = lastSunday.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // Convert long to String
        String startOfWeekString = String.valueOf(startOfWeekMillis);
        String endOfWeekString = String.valueOf(endOfWeekMillis);

        // Retrieve reservations from the last week (Monday to Sunday)
        return reservationRepository.findByStoreIdAndReservationTimestampBetween(targetId, startOfWeekString, endOfWeekString);
    }

    public Path generateCsvFromReservations(List<Reservation> reservations) throws IOException {
        Path tempFile = Files.createTempFile("reservations_last_week", ".csv");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardOpenOption.WRITE)) {
            // Write CSV header
            writer.write("ID Rezervare,ID Restaurant,ID Product,ID Client,Timp Rezervare,Instructiuni Speciale,Cantitate,PIN Rezervare,Status Rezervare,Pret Rezervare");
            writer.newLine();

            // Write data
            for (Reservation reservation : reservations) {
                writer.write(String.join(",",
                        reservation.getId().toString(),
                        reservation.getStore().getId().toString(),
                        reservation.getProduct().getId().toString(),
                        reservation.getUser().getId().toString(),
                        reservation.getReservationTimestamp(),
                        reservation.getSpecialInstructions(),
                        String.valueOf(reservation.getQuantity()),
                        reservation.getReservationPin(),
                        reservation.getReservationStatus().toString(),
                        String.valueOf(reservation.getReservationPrice())
                ));
                writer.newLine();
            }
        }

        // The temporary file will be deleted when the JVM exits
        tempFile.toFile().deleteOnExit();

        return tempFile;
    }
    @Override
    public StoreResponse storeRequestConfirm(Long storeId, String token) {
        if (!userValidationService.isAdmin(token)) {
            log.warn("Non-admin tried confirming.");

            final String existsEmail = exceptionMessageAccessor.getMessage(null, NON_ADMIN_DETECTED);
            throw new RegistrationException(existsEmail);
        }

        log.warn("Admin confirmed the request.");

        Store storeToEdit = storeRepository.findOneById(storeId);

        if (storeToEdit == null) {
            return new StoreResponse("Store not found.");
        }

        storeToEdit.setAdminConfirmed(true);
        storeToEdit.setIsDenied(false);
        storeToEdit.setDenyDetailed(null);
        storeToEdit.setDenyReason(null);

        final String storeConfirmedMessage = generalMessageAccessor.getMessage(null, STORE_CONFIRMED);

        try {
            Map<String, String> placeholders = Map.of(
                    "[COMPANY_NAME]", getValueOrDefault(storeToEdit.getCompanyName()),
                    "[HEADQUARTERS_LOCALITY]", getValueOrDefault(storeToEdit.getHeadquartersLocality()),
                    "[HEADQUARTERS_STREET]", getValueOrDefault(storeToEdit.getHeadquartersStreet()),
                    "[HEADQUARTERS_NUMBER]", getValueOrDefault(storeToEdit.getHeadquartersNr()),
                    "[COMPANY_PHONE]", getValueOrDefault(storeToEdit.getPhoneNumber()),
                    "[COMPANY_MAIL]", getValueOrDefault(storeToEdit.getUser().getEmail()),
                    "[NR_REG_COM]", getValueOrDefault(storeToEdit.getNrRegCom()),
                    "[CIF]", String.valueOf(storeToEdit.getCif()),
                    "[REPRESENTATIVE_NAME]", getValueOrDefault(storeToEdit.getUser().getFullName()),
                    "[OWNER_ROLE]", getValueOrDefault(storeToEdit.getOwnerRole())
            );

            log.info("Attempting to generate and upload contract with placeholders: " + placeholders);
            String contractUrl = contractService.generateAndUploadContract(placeholders);
            Contract contract = new Contract();
            contract.setUnsignedContractLink(contractUrl);
            contract.setStore(storeToEdit);
            contract.setContractRead(false);

            // Set the contract to the store
            storeToEdit.setContract(contract);


            // Save the contract entity
            contractRepository.save(contract);

            String to = storeToEdit.getUser().getEmail();
            String subject = "Informare despre afacerea ta";
            String message = "Salut, " + storeToEdit.getUser().getFullName() + "<br> Ne bucuram sa te anuntam ca afacerea ta a fost confirmata in platforma FoodX, iar de acum poti prelua comenzi! <br> Tot ce mai ramane de facut pentru a-ti confirma complet contul este sa citesti si sa semnezi contractul generat pe care il gasesti in panoul de control la sectiunea Documente! <br><br> <i> Daca intampini nereguli in legatura cu contractul nu ezita sa ne contactezi la </i> <a href=\"mailto:management@foodx.ro\"> management@foodx.ro. </a>";
            emailService.sendHtmlMessage(to, subject, message);

        } catch (Exception e) {
            log.error("Failed to generate contract due to an exception: ", e);
            return new StoreResponse("Failed to generate contract.");
        }

        // Save the updated store entity back to the repository
        storeRepository.save(storeToEdit);

        return new StoreResponse(storeConfirmedMessage);
    }

    @Override
    public StoreResponse storeRequestDeny(Long storeId, DenyReason denyReason, String denyDetails, String token) {
        if (!userValidationService.isAdmin(token)) {
            log.warn("Non-admin tried confirming.");

            final String existsEmail = exceptionMessageAccessor.getMessage(null, NON_ADMIN_DETECTED);
            throw new RegistrationException(existsEmail);
        }

        log.warn("Admin confirmed the request.");

        Store storeToEdit = storeRepository.findOneById(storeId);

        if (storeToEdit == null) {
            return new StoreResponse("Store not found.");
        }

        storeToEdit.setAdminConfirmed(false);
        storeToEdit.setDenyReason(denyReason);
        storeToEdit.setDenyDetailed(denyDetails);
        storeToEdit.setIsDenied(true);

        String to = storeToEdit.getUser().getEmail();
        String subject = "Informare despre afacerea ta";
        String message = "";
        if(Objects.equals(denyReason, "cannotCollaborate")){
            message = "Salut, " + storeToEdit.getUser().getFullName() + "<br> Suntem incantati de dorinta ta in a combate risipa alimentara alaturi de noi. Insa, dupa o analiza amanuntita am ajuns la concluzia ca afacerea ta nu este potrivita pentru colaborarea cu FoodX. Apreciem efortul depus in a colabora cu noi si suntem siguri ca in viitor vom putea colabora. <i> Daca consideri ca acest raspuns este o greseala nu ezita sa ne contactezi la </i> <a href=\"mailto:management@foodx.ro\"> management@foodx.ro. </a>";

        }else{
            message = "Salut, " + storeToEdit.getUser().getFullName() + "<br> Suntem incantati de dorinta ta in a combate risipa alimentara alaturi de noi. Insa, sunt necesare niste modificari pentru a putea colabora cu noi! Verifica platforma pentru detalii. <br><br> <i> Daca consideri ca acest raspuns este o greseala nu ezita sa ne contactezi la </i> <a href=\"mailto:management@foodx.ro\"> management@foodx.ro. </a>";
        }
        emailService.sendHtmlMessage(to, subject, message);

        storeRepository.save(storeToEdit);

        final String storeConfirmedMessage = generalMessageAccessor.getMessage(null, STORE_CONFIRMED);
        return new StoreResponse(storeConfirmedMessage);
    }

    @Override
    public Store storeView(Long storeId, String token) {
        if (!userValidationService.isAdmin(token)) {
            log.warn("Non-admin tried viewing.");

            final String existsEmail = exceptionMessageAccessor.getMessage(null, NON_ADMIN_DETECTED);
            throw new RegistrationException(existsEmail);
        }
        return storeRepository.findOneById(storeId);
    }

    @Override
    public List<Product> storeProducts(Long storeId, String token) {
        if (!userValidationService.isAdmin(token)) {
            log.warn("Non-admin tried viewing.");

            final String existsEmail = exceptionMessageAccessor.getMessage(null, NON_ADMIN_DETECTED);
            throw new RegistrationException(existsEmail);
        }

        Store store = storeRepository.findOneById(storeId);
        Long targetId = store.getId();
        return productRepository.findByStore_id(targetId);
    }



    @Override
    public List<Reservation> storeReservation(Long storeId, String token) {
        if (!userValidationService.isAdmin(token)) {
            log.warn("Non-admin tried viewing.");

            final String existsEmail = exceptionMessageAccessor.getMessage(null, NON_ADMIN_DETECTED);
            throw new RegistrationException(existsEmail);
        }

        Store store = storeRepository.findOneById(storeId);
        Long targetId = store.getId();

        // Calculate the timestamp for 24 hours ago
        long currentTimeMillis = Instant.now().toEpochMilli();
        long twentyFourHoursAgoMillis = currentTimeMillis - (24 * 60 * 60 * 1000);

        // Convert long to String
        String twentyFourHoursAgoString = String.valueOf(twentyFourHoursAgoMillis);

        // Retrieve reservations from the last 24 hours
        return reservationRepository.findByStoreIdAndReservationTimestampAfter(targetId, twentyFourHoursAgoString);
    }

    private String getValueOrDefault(String value) {
        return value != null ? value : "";
    }
}


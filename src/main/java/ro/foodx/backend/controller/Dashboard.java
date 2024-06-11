package ro.foodx.backend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foodx.backend.dto.category.CategoryCreateRequest;
import ro.foodx.backend.dto.category.CategoryCreateResponse;
import ro.foodx.backend.dto.dashboard.*;
import ro.foodx.backend.model.general.Menu;
import ro.foodx.backend.model.reservation.Reservation;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.model.user.UserRole;
import ro.foodx.backend.security.service.UserService;
import ro.foodx.backend.security.utils.SecurityConstants;
import ro.foodx.backend.service.*;
import org.springframework.data.domain.Pageable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin()
@RestController
@RequestMapping("/api/v1/dashboard")
public class Dashboard {

    private final GeneralService generalService;
    private final DashboardService dashboardService;
    private final UserService userService;

    private final StoreService storeService;

    private final ProductService productService;
    private final ReservationService reservationService;

    public Dashboard(GeneralService generalService, UserService userService, ProductService productService, ReservationService reservationService, StoreService storeService, DashboardService dashboardService) {
        this.generalService = generalService;
        this.userService = userService;
        this.productService = productService;
        this.reservationService = reservationService;
        this.storeService = storeService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/menu")
    public ResponseEntity<List<Menu>> getAllMenus( HttpServletRequest request) {
        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserRole role = userService.getRoleByToken(jwtToken);
        List<Menu> menus = generalService.getMenuByRole(role);
        return ResponseEntity.ok(menus);
    }

    @PostMapping("/menu")
    public ResponseEntity<MenuCreateResponse> addMenu(@Valid @RequestBody MenuCreateRequest menuCreateRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token ) {

        final MenuCreateResponse menuCreateResponse = generalService.saveMenu(menuCreateRequest, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(menuCreateResponse);
    }

    @PostMapping("/store-request-confirm")
    public ResponseEntity<StoreResponse> storeRequest(@Valid @RequestBody StoreRequestConfirm storeRequestConfirm, HttpServletRequest request ) {

        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final StoreResponse storeResponse = dashboardService.storeRequestConfirm(storeRequestConfirm.getStoreId(), jwtToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponse);
    }

    @PostMapping("/store-request-deny")
    public ResponseEntity<StoreResponse> storeRequest(@Valid @RequestBody StoreRequestDeny storeRequestDeny, HttpServletRequest request ) {

        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final StoreResponse storeResponse = dashboardService.storeRequestDeny(storeRequestDeny.getStoreId(), storeRequestDeny.getDenyReason(), storeRequestDeny.getDenyDetails(), jwtToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponse);
    }

    @GetMapping("/store-view/{storeId}")
    public ResponseEntity<Store> storeView(@Valid HttpServletRequest request, @PathVariable Long storeId) {

        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final Store storeResponse = dashboardService.storeView(storeId, jwtToken);

        return ResponseEntity.ok(storeResponse);
    }

    @GetMapping("/store-products/{storeId}")
    public ResponseEntity<List<Product>> storeProducts(@Valid HttpServletRequest request, @PathVariable Long storeId) {

        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final List<Product> storeResponse = dashboardService.storeProducts(storeId, jwtToken);

        return ResponseEntity.ok(storeResponse);
    }

    @GetMapping("/store-orders/{storeId}")
    public ResponseEntity<List<Reservation>> storeReservation(@Valid HttpServletRequest request, @PathVariable Long storeId) {

        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final List<Reservation> storeResponse = dashboardService.storeReservation(storeId, jwtToken);

        return ResponseEntity.ok(storeResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe( HttpServletRequest request) {
        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getUserByToken(jwtToken);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/my-products")
    public ResponseEntity<ApiResponse<List<Product>>> getMyProducts(HttpServletRequest request,
                                                                    @RequestParam Map<String, String> allParams,
                                                                    @PageableDefault(size = 5) Pageable pageable) {



        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        int currentPage = allParams.containsKey("current") ? Integer.parseInt(allParams.get("current")) : 1;
        int pageSize = allParams.containsKey("pageSize") ? Integer.parseInt(allParams.get("pageSize")) : 5;

        Pageable adjustedPageable = PageRequest.of(currentPage - 1, pageSize, pageable.getSort());

        Page<Product> productsPage;
        if (allParams.isEmpty()) {
            // No parameters were provided, fetch all products
            productsPage = productService.getProductsAsOwner(jwtToken, adjustedPageable);
        } else {
            // Parameters are provided, filter products accordingly
            productsPage = productService.getProductsFilteredAsOwner(jwtToken, allParams, adjustedPageable);
        }

        ApiResponse<List<Product>> response = new ApiResponse<>(
                productsPage.getContent(),
                productsPage.getNumber() + 1,
                (int) productsPage.getTotalElements(),
                true
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-stores")
    public ResponseEntity<ApiResponse<List<Store>>> getAllStores(HttpServletRequest request,
                                                                 @RequestParam Map<String, String> allParams,
                                                                 @PageableDefault(size = 5) Pageable pageable) {
        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        int currentPage = allParams.containsKey("current") ? Integer.parseInt(allParams.get("current")) : 1;
        int pageSize = allParams.containsKey("pageSize") ? Integer.parseInt(allParams.get("pageSize")) : 5;

        Pageable adjustedPageable = PageRequest.of(currentPage - 1, pageSize, pageable.getSort());

        Page<Store> storesPage;
        if (allParams.isEmpty()) {
            // No parameters were provided, fetch all products
            storesPage = storeService.getStoresAsOwner(jwtToken, adjustedPageable);
        } else {
            // Parameters are provided, filter products accordingly
            storesPage = storeService.getStoresFilteredAsOwner(jwtToken, allParams, adjustedPageable);
        }

        ApiResponse<List<Store>> response = new ApiResponse<>(
                storesPage.getContent(),
                storesPage.getNumber() + 1,
                (int) storesPage.getTotalElements(),
                true
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(HttpServletRequest request,
                                                                 @RequestParam Map<String, String> allParams,
                                                                 @PageableDefault(size = 5) Pageable pageable) {
        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        int currentPage = allParams.containsKey("current") ? Integer.parseInt(allParams.get("current")) : 1;
        int pageSize = allParams.containsKey("pageSize") ? Integer.parseInt(allParams.get("pageSize")) : 5;

        Pageable adjustedPageable = PageRequest.of(currentPage - 1, pageSize, pageable.getSort());

        Page<User> userPage;
        if (allParams.isEmpty()) {
            // No parameters were provided, fetch all products
            userPage = userService.getUsersAsOwner(jwtToken, adjustedPageable);
        } else {
            // Parameters are provided, filter products accordingly
            userPage = userService.getUsersFilteredAsOwner(jwtToken, allParams, adjustedPageable);
        }

        ApiResponse<List<User>> response = new ApiResponse<>(
                userPage.getContent(),
                userPage.getNumber() + 1,
                (int) userPage.getTotalElements(),
                true
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<Reservation>>> getMyOrders(HttpServletRequest request,
                                                                    @RequestParam Map<String, String> allParams,
                                                                    @PageableDefault(size = 5) Pageable pageable) {

        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        int currentPage = allParams.containsKey("current") ? Integer.parseInt(allParams.get("current")) : 1;
        int pageSize = allParams.containsKey("pageSize") ? Integer.parseInt(allParams.get("pageSize")) : 5;

        Pageable adjustedPageable = PageRequest.of(currentPage - 1, pageSize, pageable.getSort());

        Page<Reservation> productsPage;
        if (allParams.isEmpty()) {
            // No parameters were provided, fetch all products
            productsPage = reservationService.getReservationsAsOwner(jwtToken, adjustedPageable);
        } else {
            // Parameters are provided, filter products accordingly
            productsPage = reservationService.getReservationsFilteredAsOwner(jwtToken, allParams, adjustedPageable);
        }

        ApiResponse<List<Reservation>> response = new ApiResponse<>(
                productsPage.getContent(),
                productsPage.getNumber() + 1,
                (int) productsPage.getTotalElements(),
                true
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/download-reservations-csv/{storeId}")
    public ResponseEntity<InputStreamResource> downloadReservationsCsv(HttpServletRequest request, @PathVariable Long storeId) throws IOException {

        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Reservation> reservations = dashboardService.getLastWeekReservations(storeId, jwtToken);
        Path csvFilePath = dashboardService.generateCsvFromReservations(reservations);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(csvFilePath.toFile()));


        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFilePath.getFileName());

        System.out.println("test");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(csvFilePath.toFile().length())
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }

    @GetMapping("/my-current-orders")
    public ResponseEntity<List<Reservation>> getMyOrders(HttpServletRequest request) {

        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



        List<Reservation> myOrders = reservationService.getReservationsByStoreId(jwtToken);
        return ResponseEntity.ok(myOrders);

    }

    @GetMapping("/reservation/count")
    public long getReservationsForToday(@RequestParam Long storeId) {
        return reservationService.getConfirmedOrPendingReservationsForToday(storeId);
    }


}

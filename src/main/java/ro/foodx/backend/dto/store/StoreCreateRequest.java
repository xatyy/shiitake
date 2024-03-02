package ro.foodx.backend.dto.store;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ro.foodx.backend.model.store.StoreType;

@Getter
@Setter
@AllArgsConstructor
public class StoreCreateRequest {
    @NotNull(message = "{login_email_not_empty}")
    private Long sellerId;

    @NotEmpty(message = "{login_password_not_empty}")
    private String storeName;

    @NotEmpty(message = "{login_password_not_empty}")
    private String description;

    @NotEmpty(message = "{login_password_not_empty}")
    private String address;
    @NotEmpty(message = "{login_password_not_empty}")
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StoreType cuisineType;
    @NotEmpty(message = "{login_password_not_empty}")
    private String profilePicURL;
    @NotEmpty(message = "{login_password_not_empty}")
    private String coverPicURL;
}

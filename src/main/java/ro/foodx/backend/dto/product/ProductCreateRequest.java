package ro.foodx.backend.dto.product;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ro.foodx.backend.model.store.BagSize;

@Getter
@Setter
@AllArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "{login_email_not_empty}")
    private Long storeId;

    @NotNull(message = "{login_email_not_empty}")
    private Long categoryId;

    @NotEmpty(message = "{login_password_not_empty}")
    private String productName;

    @NotEmpty(message = "{login_password_not_empty}")
    private String productDescription;

    @NotEmpty(message = "{login_password_not_empty}")
    private Double price;

    @NotEmpty(message = "{login_password_not_empty}")
    private String productImage;

    @NotEmpty(message = "{login_password_not_empty}")
    private boolean isBag;

    @NotEmpty(message = "{login_password_not_empty}")
    private boolean isPublished;

    @NotEmpty(message = "{login_password_not_empty}")
    private int quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BagSize bagSize;

    @NotEmpty(message = "{login_password_not_empty}")
    private int customerRestriction;

    @NotEmpty(message = "{login_password_not_empty}")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String collectStart;

    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String collectEnd;


}

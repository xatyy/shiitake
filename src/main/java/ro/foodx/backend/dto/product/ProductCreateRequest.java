package ro.foodx.backend.dto.product;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ro.foodx.backend.model.store.BagType;

@Getter
@Setter
@AllArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "{login_email_not_empty}")
    private Long categoryId;

    @NotEmpty(message = "{login_password_not_empty}")
    private String productName;

    @NotEmpty(message = "{login_password_not_empty}")
    private String productDescription;

    @NotNull(message = "{login_password_not_empty}")
    private Double initialPrice;

    @NotNull(message = "{login_password_not_empty}")
    private Double finalPrice;

    @NotEmpty(message = "{login_password_not_empty}")
    private String productImage;

    @NotNull(message = "{login_password_not_empty}")
    private Boolean isPublished;

    @NotNull(message = "{login_password_not_empty}")
    private int productQuantity;

    @Enumerated(EnumType.STRING)
    private BagType bagType;

    @NotEmpty(message = "{login_password_not_empty}")
    private int productWeight;

    @NotEmpty(message = "{login_password_not_empty}")
    private String[] dietary;

    @NotEmpty(message = "{login_password_not_empty}")
    private String[] allergens;

    @NotEmpty(message = "{login_password_not_empty}")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String collectStart;

    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String collectEnd;
}

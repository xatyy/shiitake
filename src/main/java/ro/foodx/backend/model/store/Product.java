package ro.foodx.backend.model.store;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_products")
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private Long storeId;

    private String productName;

    private String productDescription;

    private Double price;

    private String productImage;

    private Long categoryId;

    private Boolean isBag;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private BagSizes bagSize;

    private int customerRestriction;


    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String collectStart;

    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String collectEnd;

}

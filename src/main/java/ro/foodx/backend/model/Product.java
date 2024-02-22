package ro.foodx.backend.model;


import jakarta.persistence.*;
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

    private String collectStart;

    private String collectEnd;

}

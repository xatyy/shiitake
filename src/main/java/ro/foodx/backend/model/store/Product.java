package ro.foodx.backend.model.store;


import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;


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

    private String productName;

    private String productDescription;

    private Double initialPrice;

    private Double finalPrice;

    private String productImage;

    @Column(nullable = false)
    private Boolean isPublished;

    private int productQuantity;
    private int productWeight;

    @Enumerated(EnumType.STRING)
    private BagType bagType;


    @Type(ListArrayType.class)
    @Column(
            name = "allergens",
            columnDefinition = "text[]"
    )
    private List<String> allergens;

    @Type(ListArrayType.class)
    @Column(
            name = "dietary",
            columnDefinition = "text[]"
    )
    private List<String> dietary;


    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String collectStart;

    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String collectEnd;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name  = "store_id", referencedColumnName = "id")
    private Store store;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name  = "category_id", referencedColumnName = "id")
    private Category category;


}

package ro.foodx.backend.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_stores")
public class Store {
    @Id
    @GeneratedValue
    private Long id;

    private Long sellerId;

    private String storeName;

    private String description;

    @Enumerated(EnumType.STRING)
    private StoreType cuisineType;

    private String profilePicURL;

    private String coverPicURL;

}

package ro.foodx.backend.model.store;


import jakarta.persistence.*;
import lombok.*;
import ro.foodx.backend.model.user.User;

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

    private String storeName;

    private String description;

    private String address;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private StoreType cuisineType;

    private String profilePicURL;

    private String coverPicURL;

    private Boolean adminConfirmed;

    private String cui;

    private String companyName;

    private String nrRegCom;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private User user;

}

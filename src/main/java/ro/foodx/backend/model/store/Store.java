package ro.foodx.backend.model.store;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import ro.foodx.backend.model.user.User;

import java.util.Set;

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

    private String locality;

    @Enumerated(EnumType.STRING)
    private StoreType cuisineType;

    private String profilePicURL;

    private String coverPicURL;

    private Boolean adminConfirmed;

    private String phoneNumber;

    private String companyPhoneNumber;

    private String companyMail;

    private String headquartersLocality;

    private String headquartersStreet;

    private String headquartersNr;

    private Long cif;

    private String companyName;

    private String nrRegCom;

    private double latitude;

    private double longitude;

    private String ownerRole;

    private Boolean isFranchise;

    private Boolean isPublished;

    private Boolean isOpen;

    private Boolean isDenied;

    private Long joinTimestamp;

    @Enumerated(EnumType.STRING)
    private DenyReason denyReason;

    private String denyDetailed;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private Set<Product> product;

    @OneToOne(mappedBy = "store")
    private Contract contract;
}

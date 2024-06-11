package ro.foodx.backend.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ro.foodx.backend.model.store.Contract;
import ro.foodx.backend.model.store.Store;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_users")
public class User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fullName;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String username;

    @JsonIgnore
    private String password;

    private Long CUI;

    private String phoneNumber;

    private Boolean isConfirmed;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne(mappedBy = "user")
    private Store store;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getCUI() {
        return CUI;
    }

    public void setCUI(Long CUI) {
        this.CUI = CUI;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}

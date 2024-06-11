package ro.foodx.backend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.ToString;

@ToString
public class RegistrationRequest {

        @NotEmpty(message = "{registration_name_not_empty}")
        private String fullName;

        @Email(message = "{registration_email_is_not_valid}")
        @NotEmpty(message = "{registration_email_not_empty}")
        private String email;

        private String username;

        @NotEmpty(message = "{registration_password_not_empty}")
        private String password;

        @NotEmpty(message = "{registration_password_not_empty}")
        private String phoneNumber;

        private Long CUI;

        public Long getCUI() { return CUI;}

        public void setCUI(Long CUI) { this.CUI = CUI;}


        public String getFullName() {
                return fullName;
        }

        public String getPhoneNumber() {
                return phoneNumber;
        }



        public String getEmail() {
                return email;
        }

        public String getUsername() {
                return email;
        }

        public String getPassword() {
                return password;
        }

        public void setFullName(String fullName) {this.fullName = fullName;}
        public void setEmail(String email) {
                this.email = email;
        }

        public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
        public void setUsername(String email) {
                this.username = email;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public RegistrationRequest(String fullName, String lastName, String email, Long CUI, String password, String phoneNumber) {
                this.fullName = fullName;
                this.CUI = CUI;
                this.email = email;
                this.username = email;
                this.password = password;
                this.phoneNumber = phoneNumber;
        }
}

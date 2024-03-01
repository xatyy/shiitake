package ro.foodx.backend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
public class RegistrationRequest {

        @NotEmpty(message = "{registration_name_not_empty}")
        private String firstName;

        @NotEmpty(message = "{registration_name_not_empty}")
        private String lastName;


        @Email(message = "{registration_email_is_not_valid}")
        @NotEmpty(message = "{registration_email_not_empty}")
        private String email;

        private String username;

        @NotEmpty(message = "{registration_password_not_empty}")
        private String password;


        public String getFirstName() {
                return firstName;
        }

        public String getLastName() {
                return lastName;
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

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public void setUsername(String email) {
                this.username = email;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public RegistrationRequest(String firstName, String lastName, String email, String username, String password) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.username = email;
                this.password = password;
        }
}

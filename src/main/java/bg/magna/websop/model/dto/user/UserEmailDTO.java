package bg.magna.websop.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserEmailDTO {
    @NotEmpty(message = "{email.notEmpty}")
    @Email(message = "{email.invalid}")
    private String email;

    public UserEmailDTO() {
    }

    public UserEmailDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

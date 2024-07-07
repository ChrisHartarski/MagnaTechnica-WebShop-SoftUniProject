package bg.magna.websop.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EditUserDTO {
    @NotEmpty(message = "{firstName.notEmpty}")
    @Size(min = 2, max = 40, message = "{firstName.length}")
    private String firstName;

    @NotEmpty(message = "{lastName.notEmpty}")
    @Size(min = 2, max = 40, message = "{lastName.length}")
    private String lastName;

    @NotEmpty(message = "{companyName.notEmpty}")
    @Size(min = 2, max = 40, message = "{companyName.length}")
    private String companyName;

    @Pattern(regexp = "[\\+]?\\d{6,15}", message = "{phone.pattern}")
    private String phone;

    public EditUserDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

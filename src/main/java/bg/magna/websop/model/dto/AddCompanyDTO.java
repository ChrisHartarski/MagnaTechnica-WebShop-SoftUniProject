package bg.magna.websop.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AddCompanyDTO {
    @NotEmpty(message = "{companyName.notEmpty}")
    @Size(min = 2, max = 40, message = "{companyName.length}")
    private String name;
    @NotEmpty(message = "{company.VATNumber.notEmpty}")
    @Size(min = 8, max = 20, message = "{company.VATNumber.invalid}")
    private String vatNumber;
    @NotEmpty(message = "{company.registeredAddress.notEmpty}")
    @Size(max = 200, message = "{company.registeredAddress.length}")
    private String registeredAddress;
    @NotEmpty(message = "{phone.notEmpty}")
    @Pattern(regexp = "[\\+]?\\d{6,15}", message = "{phone.pattern}")
    private String phone;
    @NotEmpty(message = "{email.notEmpty}")
    @Email(message = "{email.invalid}")
    private String email;

    public AddCompanyDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

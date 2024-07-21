package bg.magna.websop.model.dto.brand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AddBrandDTO {
    @NotEmpty(message = "{brand.name.notEmpty}")
    @Size(min = 2, max = 40, message = "{brand.name.length}")
    private String name;

    @Pattern(regexp = "^http[s]?:\\/\\/.[-a-zA-Z0-9@:%._\\+~#=]{2,100}\\.[a-z]{2,6}\\b[-a-zA-Z0-9@:%_\\+.~#?&\\/=]*$", message = "{imageURL.invalid}")
    @Size(max = 250, message = "{imageURL.length}")
    private String logoURL;

    public AddBrandDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }
}

package bg.magna.websop.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AddBrandDTO {
    @NotEmpty(message = "{brand.name.notEmpty}")
    @Size(min = 2, max = 40, message = "{brand.name.length}")
    private String name;

    public AddBrandDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

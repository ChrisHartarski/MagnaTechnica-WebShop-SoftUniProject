package bg.magna.websop.model.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class AddPartDTO {
    @NotEmpty(message = "{part.code.notEmpty}")
    @Size(min = 2, max = 20, message = "{part.code.length}")
    private String partCode;

    @NotEmpty(message = "{part.description.notEmpty}")
    @Size(min = 2, max = 40, message = "{part.description.length}")
    private String descriptionEn;

    @NotEmpty(message = "{part.description.notEmpty}")
    @Size(min = 2, max = 40, message = "{part.description.length}")
    private String descriptionBg;

    @Pattern(regexp = "^http[s]?:\\/\\/.[-a-zA-Z0-9@:%._\\+~#=]{2,100}\\.[a-z]{2,6}\\b[-a-zA-Z0-9@:%_\\+.~#?&\\/=]*$", message = "{imageURL.invalid}")
    @Size(max = 250, message = "{imageURL.length}")
    private String imageURL;

    @NotEmpty(message = "{brand.name.notEmpty}")
    @Size(min = 2, max = 40, message = "{brand.name.length}")
    private String brandName;

    @NotNull(message = "{part.price.notNull}")
    @Positive(message = "{part.price.positive}")
    private BigDecimal price;

    public AddPartDTO() {
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionBg() {
        return descriptionBg;
    }

    public void setDescriptionBg(String descriptionBg) {
        this.descriptionBg = descriptionBg;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

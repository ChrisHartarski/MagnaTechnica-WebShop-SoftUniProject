package bg.magna.websop.model.dto.part;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PartDataDTO {
    @NotEmpty(message = "{part.code.notEmpty}")
    @Size(min = 2, max = 20, message = "{part.code.length}")
    private String partCode;

    @NotNull(message = "{part.quantity.notEmpty}")
    @PositiveOrZero(message = "{part.quantity.positive}")
    private int quantity;

    @NotEmpty(message = "{part.description.notEmpty}")
    @Size(min = 2, max = 40, message = "{part.description.length}")
    private String descriptionEn;

    @NotEmpty(message = "{part.description.notEmpty}")
    @Size(min = 2, max = 40, message = "{part.description.length}")
    private String descriptionBg;

    @Pattern(regexp = "^http[s]?:\\/\\/.[-a-zA-Z0-9@:%._\\+~#=]{2,100}\\.[a-z]{2,6}\\b[-a-zA-Z0-9@:%_\\+.~#?&\\/=]*$", message = "{imageURL.invalid}")
    @Size(max = 250, message = "{imageURL.length}")
    private String imageURL;

    @Pattern(regexp = "^http[s]?:\\/\\/.[-a-zA-Z0-9@:%._\\+~#=]{2,100}\\.[a-z]{2,6}\\b[-a-zA-Z0-9@:%_\\+.~#?&\\/=]*$", message = "{imageURL.invalid}")
    @Size(max = 250, message = "{imageURL.length}")
    private String brandLogoURL;

    @NotEmpty(message = "{brand.name.notEmpty}")
    private String brandName;

    @NotNull(message = "{part.price.notNull}")
    @Positive(message = "{part.price.positive}")
    private BigDecimal price;

    @Size(max = 50, message = "{part.size.length}")
    private String size;

    @PositiveOrZero(message = "{part.weight.positive}")
    private double weight;

    @Size(max = 200, message = "{part.suitableFor.length}")
    private String suitableFor;

    @Size(max = 200, message = "{part.moreInfo.length}")
    private String moreInfo;

    private LocalDateTime createdOn;

    public PartDataDTO() {
    }

    public PartDataDTO(String partCode, int quantity, String descriptionEn, String descriptionBg, String imageURL, String brandLogoURL, String brandName, BigDecimal price, String size, double weight, String suitableFor, String moreInfo, LocalDateTime createdOn) {
        this.partCode = partCode;
        this.quantity = quantity;
        this.descriptionEn = descriptionEn;
        this.descriptionBg = descriptionBg;
        this.imageURL = imageURL;
        this.brandLogoURL = brandLogoURL;
        this.brandName = brandName;
        this.price = price;
        this.size = size;
        this.weight = weight;
        this.suitableFor = suitableFor;
        this.moreInfo = moreInfo;
        this.createdOn = createdOn;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getBrandLogoURL() {
        return brandLogoURL;
    }

    public void setBrandLogoURL(String brandLogoURL) {
        this.brandLogoURL = brandLogoURL;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getSuitableFor() {
        return suitableFor;
    }

    public void setSuitableFor(String suitableFor) {
        this.suitableFor = suitableFor;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}

package bg.magna.websop.model.dto.part;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShortPartDataDTO {
    private String partCode;
    private String imageURL;
    private String brandName;
    private String brandLogoURL;
    private String descriptionEn;
    private String descriptionBg;
    private BigDecimal price;
    private int quantity;
    private LocalDateTime createdOn;

    public ShortPartDataDTO() {
    }

    public ShortPartDataDTO(String partCode, String imageURL, String brandName, String brandLogoURL, String descriptionEn, String descriptionBg, BigDecimal price, int quantity, LocalDateTime createdOn) {
        this.partCode = partCode;
        this.imageURL = imageURL;
        this.brandName = brandName;
        this.brandLogoURL = brandLogoURL;
        this.descriptionEn = descriptionEn;
        this.descriptionBg = descriptionBg;
        this.price = price;
        this.quantity = quantity;
        this.createdOn = createdOn;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
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

    public String getBrandLogoURL() {
        return brandLogoURL;
    }

    public void setBrandLogoURL(String brandLogoURL) {
        this.brandLogoURL = brandLogoURL;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}

package bg.magna.websop.model.dto.machine;

import java.time.LocalDateTime;

public class ShortMachineDTO {
    private String id;
    private String name;
    private String imageURL;
    private int year;
    private String brandName;
    private String descriptionEn;
    private String descriptionBg;
    private LocalDateTime createdOn;

    public ShortMachineDTO() {
    }

    public ShortMachineDTO(String id, String name, String imageURL, int year, String brandName, String descriptionEn, String descriptionBg, LocalDateTime createdOn) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.year = year;
        this.brandName = brandName;
        this.descriptionEn = descriptionEn;
        this.descriptionBg = descriptionBg;
        this.createdOn = createdOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}

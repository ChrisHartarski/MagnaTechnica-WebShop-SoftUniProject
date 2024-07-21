package bg.magna.websop.model.dto.machine;

import jakarta.validation.constraints.*;

public class AddMachineDTO {
    @NotEmpty(message = "{machine.serialNumber.notEmpty}")
    @Size(min = 1, max = 30, message = "{machine.serialNumber.length}")
    private String serialNumber;

    @NotEmpty(message = "{machine.name.notEmpty}")
    @Size(min = 2, max = 80, message = "{machine.name.length}")
    private String name;

    @Pattern(regexp = "^http[s]?:\\/\\/.[-a-zA-Z0-9@:%._\\+~#=]{2,100}\\.[a-z]{2,6}\\b[-a-zA-Z0-9@:%_\\+.~#?&\\/=]*$", message = "{imageURL.invalid}")
    @Size(max = 250, message = "{imageURL.length}")
    private String imageURL;

    @NotNull(message = "{machine.year.notEmpty}")
    @Positive(message = "{machine.year.invalid}")
    private int year;

    @NotEmpty(message = "{brand.name.notEmpty}")
    private String brandName;

    @NotEmpty(message = "{machine.description.notEmpty}")
    @Size(min = 2, max = 80, message = "{machine.description.length}")
    private String descriptionEn;

    @NotEmpty(message = "{machine.description.notEmpty}")
    @Size(min = 2, max = 80, message = "{machine.description.length}")
    private String descriptionBg;

    @PositiveOrZero(message = "{machine.workingWidth.positive}")
    private double workingWidth;

    @PositiveOrZero(message = "{machine.weight.positive}")
    private int weight;

    @PositiveOrZero(message = "{machine.requiredPower.positive}")
    private int requiredPower;

    @Size(max = 200, message = "{machine.moreInfo.length}")
    private String moreInfoEn;

    @Size(max = 200, message = "{machine.moreInfo.length}")
    private String moreInfoBg;

    public AddMachineDTO() {
    }

    public AddMachineDTO(String serialNumber, String name, String imageURL, int year, String brandName, String descriptionEn, String descriptionBg, double workingWidth, int weight, int requiredPower, String moreInfoEn, String moreInfoBg) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.imageURL = imageURL;
        this.year = year;
        this.brandName = brandName;
        this.descriptionEn = descriptionEn;
        this.descriptionBg = descriptionBg;
        this.workingWidth = workingWidth;
        this.weight = weight;
        this.requiredPower = requiredPower;
        this.moreInfoEn = moreInfoEn;
        this.moreInfoBg = moreInfoBg;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public double getWorkingWidth() {
        return workingWidth;
    }

    public void setWorkingWidth(double workingWidth) {
        this.workingWidth = workingWidth;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getRequiredPower() {
        return requiredPower;
    }

    public void setRequiredPower(int requiredPower) {
        this.requiredPower = requiredPower;
    }

    public String getMoreInfoEn() {
        return moreInfoEn;
    }

    public void setMoreInfoEn(String moreInfoEn) {
        this.moreInfoEn = moreInfoEn;
    }

    public String getMoreInfoBg() {
        return moreInfoBg;
    }

    public void setMoreInfoBg(String moreInfoBg) {
        this.moreInfoBg = moreInfoBg;
    }
}

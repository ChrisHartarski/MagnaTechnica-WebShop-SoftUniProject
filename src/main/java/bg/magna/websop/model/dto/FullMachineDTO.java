package bg.magna.websop.model.dto;

public class FullMachineDTO {
    private String id;
    private String name;
    private String imageURL;
    private int year;
    private String brandName;
    private String descriptionEn;
    private String descriptionBg;
    private double workingWidth;
    private int weight;
    private int requiredPower;
    private String moreInfo;

    public FullMachineDTO() {
    }

    public FullMachineDTO(String id, String name, String imageURL, int year, String brandName, String descriptionEn, String descriptionBg, double workingWidth, int weight, int requiredPower, String moreInfo) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.year = year;
        this.brandName = brandName;
        this.descriptionEn = descriptionEn;
        this.descriptionBg = descriptionBg;
        this.workingWidth = workingWidth;
        this.weight = weight;
        this.requiredPower = requiredPower;
        this.moreInfo = moreInfo;
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

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }
}

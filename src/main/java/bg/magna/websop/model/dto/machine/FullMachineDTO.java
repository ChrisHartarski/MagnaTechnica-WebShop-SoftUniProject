package bg.magna.websop.model.dto.machine;

public class FullMachineDTO {
    private String id;
    private String serialNumber;
    private String name;
    private String imageURL;
    private int year;
    private String brandName;
    private String descriptionEn;
    private String descriptionBg;
    private double workingWidth;
    private int weight;
    private int requiredPower;
    private String moreInfoEn;
    private String moreInfoBg;

    public FullMachineDTO() {
    }

    public FullMachineDTO(String id, String serialNumber, String name, String imageURL, int year, String brandName, String descriptionEn, String descriptionBg, double workingWidth, int weight, int requiredPower, String moreInfoEn, String moreInfoBg) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setMoreInfoEn(String moreInfo) {
        this.moreInfoEn = moreInfo;
    }

    public String getMoreInfoBg() {
        return moreInfoBg;
    }

    public void setMoreInfoBg(String moreInfoBg) {
        this.moreInfoBg = moreInfoBg;
    }
}

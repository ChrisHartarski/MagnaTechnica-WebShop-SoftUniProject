package bg.magna.websop.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parts")
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "part_code", unique = true, nullable = false)
    private String partCode;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "description_en", nullable = false)
    private String descriptionEn;

    @Column(name = "description_bg", nullable = false)
    private String descriptionBg;

    @Column(name = "image_url")
    private String imageURL;

    @ManyToOne
    private Brand brand;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private String size;

    @Column
    private double weight;

    @Column(name = "more_info")
    private String moreInfo;

    @Column(name = "suitable_for")
    private String suitableFor;

    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;

    public Part() {
        this.createdOn = LocalDateTime.now();
    }

    public Part(String partCode, BigDecimal price, int quantity) {
        super();
        this.partCode = partCode;
        this.price = price;
        this.quantity = quantity;
    }

    public Part(String id, String partCode, int quantity, String descriptionEn, String descriptionBg, String imageURL, Brand brand, BigDecimal price, String size, double weight, String moreInfo, String suitableFor) {
        super();
        this.id = id;
        this.partCode = partCode;
        this.quantity = quantity;
        this.descriptionEn = descriptionEn;
        this.descriptionBg = descriptionBg;
        this.imageURL = imageURL;
        this.brand = brand;
        this.price = price;
        this.size = size;
        this.weight = weight;
        this.moreInfo = moreInfo;
        this.suitableFor = suitableFor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String picURL) {
        this.imageURL = picURL;
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

    public void increaseQuantity(int quantity) {
        setQuantity(getQuantity() + quantity);
    }
}

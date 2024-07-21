package bg.magna.websop.model.dto.order;

import bg.magna.websop.model.entity.Part;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class FullOrderDTO {
    private long id;
    private String userEmail;
    private String userFullName;
    private String userCompanyName;
    private Map<Part, Integer> partsAndQuantities;
    private LocalDateTime createdOn;
    private LocalDateTime dispatchedOn;
    private LocalDateTime deliveredOn;
    private String deliveryAddress;
    private String notes;
    private BigDecimal totalPrice;

    public FullOrderDTO() {
    }

    public FullOrderDTO(long id, String userEmail, String userFullName, String userCompanyName, Map<Part, Integer> partsAndQuantities, LocalDateTime createdOn, LocalDateTime dispatchedOn, LocalDateTime deliveredOn, String deliveryAddress, String notes, BigDecimal totalPrice) {
        this.id = id;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.userCompanyName = userCompanyName;
        this.partsAndQuantities = partsAndQuantities;
        this.createdOn = createdOn;
        this.dispatchedOn = dispatchedOn;
        this.deliveredOn = deliveredOn;
        this.deliveryAddress = deliveryAddress;
        this.notes = notes;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserCompanyName() {
        return userCompanyName;
    }

    public void setUserCompanyName(String userCompanyName) {
        this.userCompanyName = userCompanyName;
    }

    public Map<Part, Integer> getPartsAndQuantities() {
        return partsAndQuantities;
    }

    public void setPartsAndQuantities(Map<Part, Integer> partsAndQuantities) {
        this.partsAndQuantities = partsAndQuantities;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getDispatchedOn() {
        return dispatchedOn;
    }

    public void setDispatchedOn(LocalDateTime dispatchedOn) {
        this.dispatchedOn = dispatchedOn;
    }

    public LocalDateTime getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(LocalDateTime deliveredOn) {
        this.deliveredOn = deliveredOn;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}

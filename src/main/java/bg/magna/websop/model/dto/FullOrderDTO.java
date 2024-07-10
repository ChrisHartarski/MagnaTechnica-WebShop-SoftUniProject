package bg.magna.websop.model.dto;

import bg.magna.websop.model.entity.Part;

import java.time.Instant;
import java.util.Map;

public class FullOrderDTO {
    private long id;
    private String userEmail;
    private String userFullName;
    private String userCompanyName;
    private Map<Part, Integer> partsAndQuantities;
    private Instant createdOn;
    private Instant dispatchedOn;
    private Instant deliveredOn;
    private String deliveryAddress;
    private String notes;

    public FullOrderDTO() {
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

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getDispatchedOn() {
        return dispatchedOn;
    }

    public void setDispatchedOn(Instant dispatchedOn) {
        this.dispatchedOn = dispatchedOn;
    }

    public Instant getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(Instant deliveredOn) {
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
}

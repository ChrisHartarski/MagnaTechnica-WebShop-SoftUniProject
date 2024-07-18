package bg.magna.websop.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShortOrderDTO {
    private long id;
    private String userFullName;
    private String userCompanyName;
    private LocalDateTime createdOn;
    private LocalDateTime dispatchedOn;
    private LocalDateTime deliveredOn;
    private BigDecimal totalPrice;

    public ShortOrderDTO() {
    }

    public ShortOrderDTO(long id, String userFullName, String userCompanyName, LocalDateTime createdOn, LocalDateTime dispatchedOn, LocalDateTime deliveredOn, BigDecimal totalPrice) {
        this.id = id;
        this.userFullName = userFullName;
        this.userCompanyName = userCompanyName;
        this.createdOn = createdOn;
        this.dispatchedOn = dispatchedOn;
        this.deliveredOn = deliveredOn;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}

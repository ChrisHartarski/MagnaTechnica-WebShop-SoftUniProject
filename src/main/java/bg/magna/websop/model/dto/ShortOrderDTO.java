package bg.magna.websop.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class ShortOrderDTO {
    private long id;
    private String userFullName;
    private String userCompanyName;
    private Instant createdOn;
    private Instant dispatchedOn;
    private Instant deliveredOn;
    private BigDecimal totalPrice;

    public ShortOrderDTO() {
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}

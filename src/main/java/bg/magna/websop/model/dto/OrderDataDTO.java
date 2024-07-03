package bg.magna.websop.model.dto;

import jakarta.validation.constraints.Size;

public class OrderDataDTO {
    @Size(max = 200, message = "{order.deliveryAddress.length}")
    private String deliveryAddress;

    @Size(max = 200, message = "{order.notes.length}")
    private String notes;

    public OrderDataDTO() {
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

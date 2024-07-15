package bg.magna.websop.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class OrderDataDTO {
    @Size(min = 5, max = 200, message = "{order.deliveryAddress.length}")
    @NotEmpty(message = "{order.deliveryAddress.notEmpty}")
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

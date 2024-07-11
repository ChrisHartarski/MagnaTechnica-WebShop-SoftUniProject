package bg.magna.websop.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ElementCollection
    @MapKeyJoinColumn(name = "part_id")
    @JoinTable(name = "orders_parts",
            joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "order_quantity")
    private Map<Part, Integer> partsAndQuantities;

    @ManyToOne
    private UserEntity user;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "dispatched_on")
    private Instant dispatchedOn;

    @Column(name = "delivered_on")
    private Instant deliveredOn;

    @Column
    private String notes;

    public Order() {
        this.partsAndQuantities = new HashMap<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<Part, Integer> getPartsAndQuantities() {
        return partsAndQuantities;
    }

    public void setPartsAndQuantities(Map<Part, Integer> partsAndQuantities) {
        this.partsAndQuantities = partsAndQuantities;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getSize() {
        return getPartsAndQuantities().size();
    }

    public boolean containsPart(String partCode) {
        return (getPartsAndQuantities().keySet().stream()
                .map(Part::getPartCode)
                .anyMatch(p -> p.equals(partCode)));
    }

    public BigDecimal getTotalPrice() {
        return getPartsAndQuantities().entrySet().stream()
                .map(entry -> {
                    Part part = entry.getKey();
                    Integer quantity = entry.getValue();
                    return part.getPrice().multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isDispatched() {
        return this.dispatchedOn != null;
    }

    private boolean isDelivered() {
        return this.deliveredOn != null;
    }
}

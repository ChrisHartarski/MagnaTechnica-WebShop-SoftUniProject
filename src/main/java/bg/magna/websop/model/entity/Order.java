package bg.magna.websop.model.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(targetEntity = Part.class)
    @JoinTable(name = "orders_parts",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id"))
    @MapKey
    private Map<Part, Integer> partsAndQuantities;


    @ManyToOne
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
}

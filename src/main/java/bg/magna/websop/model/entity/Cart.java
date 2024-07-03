package bg.magna.websop.model.entity;

import bg.magna.websop.model.entity.Part;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(targetEntity = Part.class)
    @JoinTable(name = "carts_parts",
            joinColumns = @JoinColumn(name = "cart_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id"))
    @MapKey
    private Map<Part, Integer> partsAndQuantities;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    public Cart() {
        this.partsAndQuantities = new HashMap<>();
    }

    public Map<Part, Integer> getPartsAndQuantities() {
        return partsAndQuantities;
    }

    public void setPartsAndQuantities(Map<Part, Integer> partsAndQuantities) {
        this.partsAndQuantities = partsAndQuantities;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public int getSize() {
        return getPartsAndQuantities().size();
    }
}

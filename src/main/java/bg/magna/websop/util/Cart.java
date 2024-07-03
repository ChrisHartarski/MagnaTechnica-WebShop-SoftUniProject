package bg.magna.websop.util;

import bg.magna.websop.model.entity.Part;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Part, Integer> partsAndQuantities;

    public Cart() {
        this.partsAndQuantities = new HashMap<>();
    }

    public Map<Part, Integer> getPartsAndQuantities() {
        return partsAndQuantities;
    }

    public void setPartsAndQuantities(Map<Part, Integer> partsAndQuantities) {
        this.partsAndQuantities = partsAndQuantities;
    }
    public int getSize() {
        return getPartsAndQuantities().size();
    }

    public BigDecimal getArticlePrice(Part part) {
        BigDecimal price = part.getPrice();
        Integer quantity = getPartsAndQuantities().get(part);
        return price.multiply(BigDecimal.valueOf(quantity));
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
}
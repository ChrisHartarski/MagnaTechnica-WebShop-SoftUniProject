package bg.magna.websop.service;

import bg.magna.websop.model.dto.AddPartDTO;
import bg.magna.websop.model.dto.FullPartDTO;
import bg.magna.websop.model.entity.Part;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PartService {
    long getCount();

    long getTotalParts();

    boolean partExists(String partCode);

    void addPart(AddPartDTO partData);

    List<Part> getAllParts();

    void initializeMockParts() throws IOException;

    FullPartDTO getPartDTOFromPartCode(String partCode);

    Part getPartByPartCode(String partCode);

    void savePartToDB(Part part);

    void removeQuantitiesFromParts(Map<String, Integer> partsAndQuantities);

    Map<String, Part> createCartPartsMap();

    BigDecimal getCartTotalPrice();
}

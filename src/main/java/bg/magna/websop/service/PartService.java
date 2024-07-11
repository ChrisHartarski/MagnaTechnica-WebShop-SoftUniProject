package bg.magna.websop.service;

import bg.magna.websop.model.dto.PartDataDTO;
import bg.magna.websop.model.dto.ShortPartDataDTO;
import bg.magna.websop.model.entity.Part;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PartService {
    long getCount();

    long getTotalParts();

    boolean partExists(String partCode);

    void addPart(PartDataDTO partData);

    void editPart(PartDataDTO partData);

    void deletePart(String partCode);

    List<ShortPartDataDTO> getAllShortPartDTOs();

    void initializeMockParts() throws IOException;

    PartDataDTO getPartDTOFromPartCode(String partCode);

    Part getPartByPartCode(String partCode);

    void savePartToDB(Part part);

    void removeQuantitiesFromParts(Map<Part, Integer> partsAndQuantities);

    BigDecimal getCartTotalPrice(String userId);
}

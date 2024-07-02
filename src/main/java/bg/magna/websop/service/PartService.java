package bg.magna.websop.service;

import bg.magna.websop.model.dto.AddPartDTO;
import bg.magna.websop.model.entity.Part;

import java.util.List;

public interface PartService {
    long getCount();

    long getTotalParts();

    boolean partExists(String partCode);

    void addPart(AddPartDTO partData);

    List<Part> getAllParts();
}

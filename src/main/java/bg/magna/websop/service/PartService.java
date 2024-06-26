package bg.magna.websop.service;

import bg.magna.websop.model.dto.AddPartDTO;

public interface PartService {
    long getCount();

    long getTotalParts();

    boolean partExists(String partCode);

    void addPart(AddPartDTO partData);
}

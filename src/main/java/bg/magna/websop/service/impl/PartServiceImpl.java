package bg.magna.websop.service.impl;

import bg.magna.websop.model.entity.Part;
import bg.magna.websop.repository.PartRepository;
import bg.magna.websop.service.PartService;
import org.springframework.stereotype.Service;

@Service
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;

    public PartServiceImpl(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Override
    public long getCount() {
        return partRepository.count();
    }

    @Override
    public long getTotalParts() {
        return partRepository.findAll().stream()
                .mapToInt(Part::getQuantity)
                .sum();
    }
}

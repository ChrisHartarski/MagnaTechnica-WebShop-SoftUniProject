package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.AddPartDTO;
import bg.magna.websop.model.entity.Brand;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.repository.PartRepository;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.PartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final BrandService brandService;
    private final ModelMapper modelMapper;

    public PartServiceImpl(PartRepository partRepository, BrandService brandService, ModelMapper modelMapper) {
        this.partRepository = partRepository;
        this.brandService = brandService;
        this.modelMapper = modelMapper;
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

    @Override
    public boolean partExists(String partCode) {
        return partRepository.existsByPartCode(partCode);
    }

    @Override
    public void addPart(AddPartDTO partData) {
        Part part = modelMapper.map(partData, Part.class);
        part.setQuantity(0);
        Brand brand = brandService.getBrandByName(partData.getBrandName());
        part.setBrand(brand);
        partRepository.saveAndFlush(part);
    }

    @Override
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }
}

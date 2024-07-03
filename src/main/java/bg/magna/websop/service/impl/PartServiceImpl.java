package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.AddPartDTO;
import bg.magna.websop.model.dto.FullPartDTO;
import bg.magna.websop.model.entity.Brand;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.repository.PartRepository;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.PartService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final BrandService brandService;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private static final String PARTS_JSON_FILE_PATH = "src/main/resources/data/parts.json";

    public PartServiceImpl(PartRepository partRepository, BrandService brandService, ModelMapper modelMapper, Gson gson) {
        this.partRepository = partRepository;
        this.brandService = brandService;
        this.modelMapper = modelMapper;
        this.gson = gson;
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

    @Override
    public void initializeMockParts() throws IOException {
        if(partRepository.count() == 0) {
            Arrays.stream(gson.fromJson(Files.readString(Path.of(PARTS_JSON_FILE_PATH)), AddPartDTO[].class))
                    .map(dto -> {
                        Part part = modelMapper.map(dto, Part.class);
                        part.setQuantity(20);
                        part.setBrand(brandService.getBrandByName(dto.getBrandName()));
                        return part;
                    })
                    .forEach(p -> {
                        if(!partRepository.existsByPartCode(p.getPartCode())) {
                            partRepository.saveAndFlush(p);
                        }
                    });
        }
    }

    @Override
    public FullPartDTO getPartDTOFromPartCode(String partCode) {
        if (partExists(partCode)) {
            Part part = getPartByPartCode(partCode);
            return modelMapper.map(part, FullPartDTO.class);
        }
        return null;
    }

    @Override
    public Part getPartByPartCode(String partCode) {
        return partRepository.getByPartCode(partCode);
    }

    @Override
    public void savePartToDB(Part part) {
        partRepository.saveAndFlush(part);
    }
}

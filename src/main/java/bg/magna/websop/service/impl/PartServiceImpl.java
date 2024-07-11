package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.PartDataDTO;
import bg.magna.websop.model.dto.ShortPartDataDTO;
import bg.magna.websop.model.entity.Brand;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.repository.PartRepository;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.PartService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final BrandService brandService;
    private final UserSession userSession;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private static final String PARTS_JSON_FILE_PATH = "src/main/resources/data/parts.json";

    public PartServiceImpl(PartRepository partRepository, BrandService brandService, UserSession userSession, ModelMapper modelMapper, Gson gson) {
        this.partRepository = partRepository;
        this.brandService = brandService;
        this.userSession = userSession;
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
    public void addPart(PartDataDTO partData) {
        Part part = modelMapper.map(partData, Part.class);
        Brand brand = brandService.getBrandByName(partData.getBrandName());
        part.setBrand(brand);
        partRepository.saveAndFlush(part);
    }

    @Override
    public void editPart(PartDataDTO partData) {
        Part part = getPartByPartCode(partData.getPartCode());

        modelMapper.map(partData, part);
        Brand brand = brandService.getBrandByName(partData.getBrandName());
        part.setBrand(brand);
        partRepository.saveAndFlush(part);
    }

    @Override
    @Transactional
    public void deletePart(String partCode) {
        partRepository.deleteByPartCode(partCode);
    }

    @Override
    public List<ShortPartDataDTO> getAllShortPartDTOs() {
        return partRepository.findAll().stream()
                .map(part -> modelMapper.map(part, ShortPartDataDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void initializeMockParts() throws IOException {
        if(partRepository.count() == 0) {
            Arrays.stream(gson.fromJson(Files.readString(Path.of(PARTS_JSON_FILE_PATH)), PartDataDTO[].class))
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
    public PartDataDTO getPartDTOFromPartCode(String partCode) {
        if (partExists(partCode)) {
            Part part = getPartByPartCode(partCode);
            return modelMapper.map(part, PartDataDTO.class);
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

    @Override
    public void removeQuantitiesFromParts(Map<String, Integer> partsAndQuantities) {
        partsAndQuantities.forEach((partCode, quantity) -> {
            Part part = getPartByPartCode(partCode);
            part.setQuantity(part.getQuantity() - quantity);
            savePartToDB(part);
        });
    }

    @Override
    public Map<String, Part> createPartCodeMap() {
        Map<String, Part> cartPartsMap = new HashMap<>();
        Set<String> partCodes = userSession.getCart().getPartsAndQuantities().keySet();
        partCodes.forEach(partCode -> cartPartsMap.put(partCode, getPartByPartCode(partCode)));
        return cartPartsMap;
    }

    @Override
    public BigDecimal getCartTotalPrice() {
        Map<String, Part> cartPartsMap = createPartCodeMap();
        Map<String, Integer> cartPartsAndQuantities = userSession.getCart().getPartsAndQuantities();

        return cartPartsMap.entrySet().stream()
                .map(entry ->
                    entry.getValue().getPrice()
                            .multiply(BigDecimal.valueOf(cartPartsAndQuantities.get(entry.getKey()))))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

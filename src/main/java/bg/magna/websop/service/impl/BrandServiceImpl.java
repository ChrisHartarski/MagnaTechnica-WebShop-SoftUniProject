package bg.magna.websop.service.impl;

import bg.magna.websop.model.entity.Brand;
import bg.magna.websop.repository.BrandRepository;
import bg.magna.websop.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public boolean brandRepositoryIsEmpty() {
        return brandRepository.count() == 0;
    }

    @Override
    public void addBrand(String name) {
        if(!brandRepository.existsByName(name)) {
            brandRepository.saveAndFlush(new Brand(name));
        }
    }

    @Override
    public long getCount() {
        return brandRepository.count();
    }

    @Override
    public boolean brandExists(String name) {
        return brandRepository.existsByName(name);
    }

    @Override
    public List<String> getAllBrandNames() {
        return brandRepository.findAll().stream().map(Brand::getName).collect(Collectors.toList());
    }

    @Override
    public Brand getBrandByName(String brandName) {
        return brandRepository.getByName(brandName);
    }
}

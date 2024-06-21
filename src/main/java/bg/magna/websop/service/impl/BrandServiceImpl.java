package bg.magna.websop.service.impl;

import bg.magna.websop.model.entity.Brand;
import bg.magna.websop.repository.BrandRepository;
import bg.magna.websop.service.BrandService;
import org.springframework.stereotype.Service;

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
}

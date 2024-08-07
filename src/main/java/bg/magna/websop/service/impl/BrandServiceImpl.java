package bg.magna.websop.service.impl;

import bg.magna.websop.model.entity.Brand;
import bg.magna.websop.repository.BrandRepository;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
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
    public Brand findBrandByName(String brandName) {
        return brandRepository
                .findByName(brandName)
                .orElseThrow(() -> new ResourceNotFoundException("Brand with name " + brandName + "does not exist."));
    }

    @Override
    public void addBrand(String name, String logoURL) {
        if(!brandRepository.existsByName(name)) {
            brandRepository.saveAndFlush(new Brand(name, logoURL));
        } else throw new IllegalArgumentException("Brand with name " + name + " already exists.");
    }

    @Override
    public void initializeMockBrands() {
        if (brandRepository.count() == 0){
            addBrand("John Deere", "https://upload.wikimedia.org/wikipedia/en/thumb/c/cf/John_Deere_logo.svg/330px-John_Deere_logo.svg.png");
            addBrand("Massey Ferguson", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/MF_Logo_2022.png/330px-MF_Logo_2022.png");
            addBrand("New Holland", "https://upload.wikimedia.org/wikipedia/en/thumb/7/74/New_Holland_Logo_2023.png/330px-New_Holland_Logo_2023.png");
            addBrand("Ferruzza", "http://www.ferruzza.it/sito/wp-content/uploads/2016/09/logoa.gif");
            addBrand("Manitou", "https://upload.wikimedia.org/wikipedia/commons/8/8a/Logo_Manitou_Group.png?20171005085211");
            addBrand("Friuli", "https://www.agricolmeccanica.it/wp-content/uploads/2020/11/FRIULI-logo-corretto.png");
            addBrand("Akpil", "https://www.akpil.pl/wp-content/uploads/2024/04/logo-akpil-1975.jpg");
            addBrand("Arbos", "https://agrimar.pl/wp-content/uploads/2018/02/logo-arbos-www.png");
        }
    }
}

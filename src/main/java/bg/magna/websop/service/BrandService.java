package bg.magna.websop.service;

import bg.magna.websop.model.entity.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    long getCount();
    boolean brandExists(String name);
    List<String> getAllBrandNames();
    Brand findBrandByName(String brandName);
    void addBrand(String name, String logoURL);
    void initializeMockBrands();
}

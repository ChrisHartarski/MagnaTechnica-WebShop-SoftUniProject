package bg.magna.websop.service;

import bg.magna.websop.model.entity.Brand;

import java.util.List;

public interface BrandService {
    long getCount();
    boolean brandExists(String name);
    List<String> getAllBrandNames();
    Brand getBrandByName(String brandName);
    void initializeMockBrands();
    void addBrand(String name);
}

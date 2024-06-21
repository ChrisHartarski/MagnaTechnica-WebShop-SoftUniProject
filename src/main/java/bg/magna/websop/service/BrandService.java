package bg.magna.websop.service;

public interface BrandService {
    boolean brandRepositoryIsEmpty();
    void addBrand(String name);
    long getCount();
}

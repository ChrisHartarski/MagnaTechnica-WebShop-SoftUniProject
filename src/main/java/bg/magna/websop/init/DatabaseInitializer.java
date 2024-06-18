package bg.magna.websop.init;

import bg.magna.websop.model.entity.User;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final BrandService brandService;
    private final UserService userService;

    public DatabaseInitializer(BrandService brandService, UserService userService) {
        this.brandService = brandService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.userRepositoryIsEmpty()){
            userService.addAdminUser();
        }

        if (brandService.brandRepositoryIsEmpty()){
            brandService.addBrand("John Deere");
            brandService.addBrand("Massey Ferguson");
            brandService.addBrand("New Holland");
            brandService.addBrand("Claas");
            brandService.addBrand("Manitou");
            brandService.addBrand("Case IH");
            brandService.addBrand("Deutz");
        }
    }
}

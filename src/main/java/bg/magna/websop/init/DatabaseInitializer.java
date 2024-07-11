package bg.magna.websop.init;

import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.CompanyService;
import bg.magna.websop.service.UserEntityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final BrandService brandService;
    private final UserEntityService userService;
    private final CompanyService companyService;

    public DatabaseInitializer(BrandService brandService, UserEntityService userService, CompanyService companyService) {
        this.brandService = brandService;
        this.userService = userService;
        this.companyService = companyService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.userRepositoryIsEmpty()){
            companyService.addfirstTwoCompanies();
            userService.addAdminUser();
            userService.addFirstUser();
        }
    }
}

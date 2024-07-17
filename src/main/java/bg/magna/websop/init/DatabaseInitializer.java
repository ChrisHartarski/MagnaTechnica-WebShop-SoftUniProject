package bg.magna.websop.init;

import bg.magna.websop.service.CompanyService;
import bg.magna.websop.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final UserService userService;
    private final CompanyService companyService;

    public DatabaseInitializer(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.userRepositoryIsEmpty()){
            companyService.addFirstTwoCompanies();
            userService.addAdminUser();
            userService.addFirstUser();
        }
    }
}

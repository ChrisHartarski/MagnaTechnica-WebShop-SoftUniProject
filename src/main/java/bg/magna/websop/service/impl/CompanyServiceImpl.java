package bg.magna.websop.service.impl;

import bg.magna.websop.model.entity.Company;
import bg.magna.websop.repository.CompanyRepository;
import bg.magna.websop.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void addfirstTwoCompanies() {
        Company magna = new Company("Magna Technica Ltd.", "BG203779968", "Bulgaria, Ruse, 26 Solun str.", "+359 893 333 595", "office@magna.bg");
        Company company1 = new Company("Company 1", "BG123456789", "Bulgaria, Sofia, 1 Sofia blvd.", "+359 888 888 888", "office@company1");
        companyRepository.saveAllAndFlush(List.of(magna, company1));
    }

    @Override
    public Company getCompanyByName(String name) {
        return companyRepository.getByName(name);
    }
}

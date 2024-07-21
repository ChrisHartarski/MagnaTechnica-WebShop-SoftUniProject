package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.company.AddCompanyDTO;
import bg.magna.websop.model.entity.Company;
import bg.magna.websop.repository.CompanyRepository;
import bg.magna.websop.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, ModelMapper modelMapper) {
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean companyExists(String companyName) {
        return companyRepository.existsByName(companyName);
    }

    @Override
    public boolean companyWithVATExists(String vatNumber) {
        return companyRepository.existsByVatNumber(vatNumber);
    }

    @Override
    public Company getCompanyByName(String name) {
        return companyRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("Company with name " + name + " not found"));
    }

    @Override
    public void addCompany(AddCompanyDTO companyData) {
        if (!companyExists(companyData.getName())) {
            companyRepository.saveAndFlush(modelMapper.map(companyData, Company.class));
        } else throw new IllegalArgumentException("Company with name " + companyData.getName() + " already exists");
    }

    @Override
    public void addFirstTwoCompanies() {
        if (companyRepository.count() == 0) {
            Company magna = new Company("Magna Technica Ltd.", "BG203779968", "Bulgaria, Ruse, 26 Solun str.", "+359 893 333 595", "office@magna.bg");
            Company company1 = new Company("Company 1", "BG123456789", "Bulgaria, Sofia, 1 Sofia blvd.", "+359 888 888 888", "office@company1");
            companyRepository.saveAllAndFlush(List.of(magna, company1));
        }
    }
}

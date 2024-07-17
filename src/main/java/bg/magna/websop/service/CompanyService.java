package bg.magna.websop.service;

import bg.magna.websop.model.dto.AddCompanyDTO;
import bg.magna.websop.model.entity.Company;

public interface CompanyService {
    boolean companyExists(String companyName);

    boolean companyWithVATExists(String vatNumber);

    Company getCompanyByName(String name);

    void addCompany(AddCompanyDTO companyData);

    void addFirstTwoCompanies();
}

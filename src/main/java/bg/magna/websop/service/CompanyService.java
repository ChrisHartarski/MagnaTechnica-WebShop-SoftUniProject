package bg.magna.websop.service;

import bg.magna.websop.model.dto.AddCompanyDTO;
import bg.magna.websop.model.entity.Company;

public interface CompanyService {
    void addfirstTwoCompanies();

    Company getCompanyByName(String name);

    boolean companyExists(String companyName);

    boolean companyWithVATExists(String vatNumber);

    void addCompany(AddCompanyDTO companyData);
}

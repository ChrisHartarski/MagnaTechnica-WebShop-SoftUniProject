package bg.magna.websop.controller;

import bg.magna.websop.model.entity.Company;
import bg.magna.websop.repository.CompanyRepository;
import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.CompanyService;
import bg.magna.websop.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        companyRepository.deleteAll();

        companyService.addFirstTwoCompanies();
        userService.addAdminUser();
        userService.addFirstUser();
    }

    @Test
    public void testAddCompanyGet() throws Exception {
        mockMvc.perform(get("/companies/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-company"));
    }

    @Test
    public void testAddCompanyPost_addsCompany() throws Exception {
        Company testCompany = createTestCompany("testCompany", "testVATNumber");

        mockMvc.perform(post("/companies/add")
                        .param("name", testCompany.getName())
                        .param("vatNumber", testCompany.getVatNumber())
                        .param("registeredAddress", testCompany.getRegisteredAddress())
                        .param("email", testCompany.getEmail())
                        .param("phone", testCompany.getPhone())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));

        Company actual = companyRepository.findByName(testCompany.getName()).orElse(null);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(testCompany.getName(), actual.getName());
        Assertions.assertEquals(testCompany.getVatNumber(), actual.getVatNumber());
        Assertions.assertEquals(testCompany.getRegisteredAddress(), actual.getRegisteredAddress());
        Assertions.assertEquals(testCompany.getEmail(), actual.getEmail());
        Assertions.assertEquals(testCompany.getPhone(), actual.getPhone());
    }

    @Test
    public void testAddCompanyPost_returnsIfInputIsInvalid() throws Exception {
        Company testCompany = createTestCompanyAndSaveToDB("", "testVATNumber");

        mockMvc.perform(post("/companies/add")
                        .param("name", testCompany.getName())
                        .param("vatNumber", testCompany.getVatNumber())
                        .param("registeredAddress", testCompany.getRegisteredAddress())
                        .param("email", testCompany.getEmail())
                        .param("phone", testCompany.getPhone())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies/add"));
    }

    @Test
    public void testAddCompanyPost_returnsIfCompanyNameExists() throws Exception {
        Company testCompany = createTestCompanyAndSaveToDB("testCompany", "testVATNumber");

        mockMvc.perform(post("/companies/add")
                        .param("name", testCompany.getName())
                        .param("vatNumber", "testVATNumber2")
                        .param("registeredAddress", testCompany.getRegisteredAddress())
                        .param("email", testCompany.getEmail())
                        .param("phone", testCompany.getPhone())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies/add"))
                .andExpect(flash().attribute("companyExists", true));
    }

    @Test
    public void testAddCompanyPost_returnsIfCompanyVATExists() throws Exception {
        Company testCompany = createTestCompanyAndSaveToDB("testCompany", "testVATNumber");

        mockMvc.perform(post("/companies/add")
                        .param("name", "testCompany2")
                        .param("vatNumber", testCompany.getVatNumber())
                        .param("registeredAddress", testCompany.getRegisteredAddress())
                        .param("email", testCompany.getEmail())
                        .param("phone", testCompany.getPhone())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies/add"))
                .andExpect(flash().attribute("VATnumberExists", true));
    }

    private Company createTestCompany(String name, String vat) {
        return new Company(
                name,
                vat,
                "address",
                "0888888888",
                "email@example.com");
    }

    private Company createTestCompanyAndSaveToDB(String name, String vat) {
        Company company = createTestCompany(name, vat);

        return companyRepository.saveAndFlush(company);
    }
}

package bg.magna.websop.controller;

import bg.magna.websop.model.dto.machine.AddMachineDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.entity.*;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.*;
import bg.magna.websop.service.CompanyService;
import bg.magna.websop.service.MachineService;
import bg.magna.websop.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminPanelControllerIT {
    private static final Brand TEST_BRAND = new Brand("brand1", "https://example.com/exampleLogo.png");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Mock
    private MachineService machineService;


    @AfterEach
    public void tearDown() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        partRepository.deleteAll();
        companyRepository.deleteAll();
        brandRepository.deleteAll();

        companyService.addFirstTwoCompanies();
        userService.addAdminUser();
        userService.addFirstUser();
    }

    @Test
    public void testViewAdminPanel() throws Exception {
        brandRepository.saveAndFlush(TEST_BRAND);

        Company testCompany = new Company("TestCompany", "TestVAT", "TestAddress", "TestPhone", "testCompany@example.com");
        companyRepository.saveAndFlush(testCompany);

        Part testPart = new Part("UUID1", "partCode", 5, "descriptionEn", "descriptionBg", "imageURL", brandRepository.findByName(TEST_BRAND.getName()).get(), new BigDecimal("20"), "size", 0, "moreInfo", "suitableFor");
        partRepository.saveAndFlush(testPart);

        UserEntity testUser = new UserEntity("someUUID", "testUser@example.com", "password", "Test", "User", "0888888888", UserRole.USER, new HashMap<>(), new ArrayList<>(), companyRepository.findByName(testCompany.getName()).get());
        userRepository.saveAndFlush(testUser);

        Order testOrderAwaiting = new Order(1, Map.of(partRepository.getByPartCode(testPart.getPartCode()), 5), userRepository.findByEmail(testUser.getEmail()).get(), "address", LocalDateTime.of(2024, 4, 12, 12, 35), null, null, "some notes");
        Order testOrderDispatched = new Order(2, Map.of(partRepository.getByPartCode(testPart.getPartCode()), 5), userRepository.findByEmail(testUser.getEmail()).get(), "address", LocalDateTime.of(2024, 3, 10, 10, 30), LocalDateTime.of(2024, 3, 12, 10, 30), null, "some notes");
        Order testOrderDelivered = new Order(3, Map.of(partRepository.getByPartCode(testPart.getPartCode()), 5), userRepository.findByEmail(testUser.getEmail()).get(), "address", LocalDateTime.of(2024, 2, 5, 11, 20), LocalDateTime.of(2024, 2, 6, 10, 30), LocalDateTime.of(2024, 2, 7, 9, 45), "some notes");
        orderRepository.saveAllAndFlush(List.of(testOrderAwaiting, testOrderDispatched, testOrderDelivered));

        mockMvc.perform(get("/admin-panel")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(view().name("admin-panel"))
                .andExpect(model().attribute("usersCount", 3L))
                .andExpect(model().attribute("adminRolesCount", 1L))
                .andExpect(model().attribute("userRolesCount", 2L))
                .andExpect(model().attribute("brandsCount", 1L))
                .andExpect(model().attribute("partArticlesCount", 1L))
                .andExpect(model().attribute("totalPartsCount", 5L))
                .andExpect(model().attribute("awaitingOrdersCount", 1))
                .andExpect(model().attribute("dispatchedOrdersCount", 1))
                .andExpect(model().attribute("deliveredOrdersCount", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddBrandPost() throws Exception {

        mockMvc.perform(post("/brands/add")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("name", TEST_BRAND.getName())
                        .param("logoURL", TEST_BRAND.getLogoURL())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-panel"));

        Optional<Brand> brandOpt = brandRepository.findByName(TEST_BRAND.getName());

        Assertions.assertTrue(brandOpt.isPresent());

        Brand brand = brandOpt.get();

        Assertions.assertEquals(TEST_BRAND.getName(), brand.getName());
        Assertions.assertEquals(TEST_BRAND.getLogoURL(), brand.getLogoURL());
    }

    @Test
    public void testAddBrandPost_returnsToAddBrandWhenInputInvalid() throws Exception {
        mockMvc.perform(post("/brands/add")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("name", "")
                        .param("logoURL", "")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/brands/add"));
    }

    @Test
    public void testAddBrandPost_returnsToAddBrandBrandExists() throws Exception {
        brandRepository.saveAndFlush(new Brand(TEST_BRAND.getName(), TEST_BRAND.getLogoURL()));

        mockMvc.perform(post("/brands/add")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("name", TEST_BRAND.getName())
                        .param("logoURL", TEST_BRAND.getLogoURL())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/brands/add"));
    }

    @Test
    public void testInitializeDB_withoutMachines() throws Exception {
        when(machineService.addMachine(any(AddMachineDTO.class))).thenReturn(new FullMachineDTO());

        mockMvc.perform(post("/admin-panel/initializeMockDB")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Assertions.assertEquals(8, brandRepository.count());
        Assertions.assertEquals(21, partRepository.count());
    }
}

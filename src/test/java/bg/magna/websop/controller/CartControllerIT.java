package bg.magna.websop.controller;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.entity.*;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.*;
import bg.magna.websop.service.UserService;
import bg.magna.websop.service.helper.UserHelperService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BrandRepository brandRepository;

    @AfterEach
    public void tearDown() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        partRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testAddOrder() throws Exception {
        brandRepository.saveAndFlush(createTestBrand());
        Brand brand = brandRepository.findByName("brand1").orElse(null);

        companyRepository.saveAndFlush(createTestCompany());
        Company company = companyRepository.findByName("company1").orElse(null);

        partRepository.saveAndFlush(createTestPart(brand));
        Part part = partRepository.findByPartCode("partCode").orElse(null);

        UserEntity user = createTestUser(company);
        user.getCart().putIfAbsent(part, 5);
        userRepository.saveAndFlush(user);
        user = userRepository.findByEmail("testUser@example.com").orElse(null);

        Order expected = createAwaitingOrder(user);

        mockMvc.perform(post("/cart/add-order")
                .with(user(user.getEmail()))
                .with(csrf())
                        .param("deliveryAddress", "address")
                        .param("notes", "some notes"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));


        //added correct order
        Assertions.assertEquals(1, orderRepository.count());
        Order actual = orderRepository.findAll().getFirst();
        Assertions.assertEquals(expected.getPartsAndQuantities(), actual.getPartsAndQuantities());
        Assertions.assertEquals(expected.getUser(), actual.getUser());
        Assertions.assertEquals("address", actual.getDeliveryAddress());
        Assertions.assertEquals("some notes", actual.getNotes());

        //remove quantities from parts
        Assertions.assertEquals(15, partRepository.getByPartCode("partCode").getQuantity());

        //remove items from user cart
        Assertions.assertEquals(0, user.getCartSize());
    }

    @Test
    @Transactional
    public void testDeleteItemFromCart() throws Exception {
        brandRepository.saveAndFlush(createTestBrand());
        Brand brand = brandRepository.findByName("brand1").orElse(null);

        companyRepository.saveAndFlush(createTestCompany());
        Company company = companyRepository.findByName("company1").orElse(null);

        partRepository.saveAndFlush(createTestPart(brand));
        Part part = partRepository.findByPartCode("partCode").orElse(null);

        Part part2 = createTestPart(brand);
        part2.setPartCode("partCode2");
        partRepository.saveAndFlush(part2);
        part2 = partRepository.findByPartCode("partCode2").orElse(null);


        UserEntity user = createTestUser(company);
        user.getCart().put(part, 5);
        user.getCart().put(part2, 2);
        userRepository.saveAndFlush(user);
        user = userRepository.findByEmail("testUser@example.com").orElse(null);

        mockMvc.perform(delete("/cart/remove-item/" + part.getPartCode())
                        .with(user(user.getEmail()).roles("USER"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    private Brand createTestBrand() {
        return new Brand("brand1", "https://example.com/exampleLogo.png");
    }

    private Company createTestCompany() {
        return new Company(
                "company1",
                "VAT",
                "address",
                "phone",
                "email");
    }

    private Part createTestPart(Brand brand) {
        return new Part(
                "UUID1",
                "partCode",
                20,
                "descriptionEn",
                "descriptionBg",
                "imageURL",
                brand,
                new BigDecimal("20"),
                "size",
                0,
                "moreInfo",
                "suitableFor");
    }

    private UserEntity createTestUser(Company company){
        return new UserEntity(
                "someUUID",
                "testUser@example.com",
                "password",
                "Test",
                "User",
                "0888888888",
                UserRole.USER,
                new HashMap<>(),
                new ArrayList<>(),
                company);
    }


    private Order createAwaitingOrder(UserEntity user) {
        return new Order(
                user.getCart(),
                user,
                "address",
                LocalDateTime.of(2024, 4, 12, 12, 35),
                null,
                null,
                "some notes");
    }

    private CurrentUserDetails getCurrentUserDetails(UserEntity user) {
        return new CurrentUserDetails(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole())),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                "company1",
                5
        );
    }

}

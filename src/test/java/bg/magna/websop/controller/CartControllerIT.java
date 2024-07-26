package bg.magna.websop.controller;

import bg.magna.websop.model.entity.*;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.*;
import bg.magna.websop.service.CompanyService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.service.helper.UserHelperService;
import bg.magna.websop.service.impl.CurrentUserDetailsService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @MockBean
    private CurrentUserDetailsService currentUserDetailsService;
    @Autowired
    private UserHelperService userHelperService;

    @AfterEach
    public void tearDown() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        partRepository.deleteAll();
        brandRepository.deleteAll();
        companyRepository.deleteAll();

        companyService.addFirstTwoCompanies();
        userService.addAdminUser();
        userService.addFirstUser();
    }

    @Test
    public void viewCart() throws Exception {
        mockMvc.perform(get("/cart")
                        .with(user("user01@example.com").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"));
    }

    @Test
    @Transactional
    public void testAddOrder() throws Exception {
        Brand brand = createTestBrand();

        Company company = createTestCompany();

        Part part = createTestPart(brand, "partCode");

        Map<Part, Integer> cart = new HashMap<>();
        cart.put(part, 5);
        UserEntity user = createTestUser(company, "user1@example.com", cart);

        Order expected = createAwaitingOrder(user, "address", "some notes");

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
    public void testAddOrder_returnsIfInputInvalid() throws Exception {
        Brand brand = createTestBrand();

        Company company = createTestCompany();

        Part part = createTestPart(brand, "partCode");

        Map<Part, Integer> cart = new HashMap<>();
        cart.put(part, 5);
        UserEntity user = createTestUser(company, "user1@example.com", cart);

        Order expected = createAwaitingOrder(user, "address", "some notes");

        mockMvc.perform(post("/cart/add-order")
                        .with(user(user.getEmail()))
                        .with(csrf())
                        .param("deliveryAddress", "")
                        .param("notes", "some notes"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        //no order added
        Assertions.assertEquals(0, orderRepository.count());
    }

    @Test
    @Transactional
    public void testAddOrder_returnsIfCartIsEmpty() throws Exception {
        Brand brand = createTestBrand();

        Company company = createTestCompany();

        Part part = createTestPart(brand, "partCode");

        Map<Part, Integer> cart = new HashMap<>();
        UserEntity user = createTestUser(company, "user1@example.com", cart);

        Order expected = createAwaitingOrder(user, "address", "some notes");

        mockMvc.perform(post("/cart/add-order")
                        .with(user(user.getEmail()))
                        .with(csrf())
                        .param("deliveryAddress", "address")
                        .param("notes", "some notes"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(flash().attribute("cartEmpty", true));


        //no order added
        Assertions.assertEquals(0, orderRepository.count());
    }

    @Test
    @Transactional
    public void testDeleteItemFromCart() throws Exception {
//        @AuthenticationPrincipal

        Brand brand = createTestBrand();
        Company company = createTestCompany();

        Part part = createTestPart(brand, "partCode");
        Part part2 = createTestPart(brand, "partCode2");

        UserEntity user = userRepository.findByEmail("user01@example.com").orElse(null);
        Map<Part, Integer> cart = new HashMap<>();
        cart.put(part, 5);
        cart.put(part2, 2);
        user.setCart(cart);
        userRepository.saveAndFlush(user);

        mockMvc.perform(delete("/cart/remove-item/" + part.getPartCode())
                        .with(user(user.getEmail()).roles("USER"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        UserEntity actual = userRepository.findByEmail("user01@example.com").orElse(null);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.getCartSize());
        Assertions.assertTrue(actual.getCart().containsKey(part2));
        Assertions.assertEquals(2, actual.getCart().get(part2));
    }

    private Brand createTestBrand() {
        Brand brand = new Brand("brand1", "https://example.com/exampleLogo.png");
        brandRepository.saveAndFlush(brand);
        return brandRepository.findByName("brand1").orElse(null);
    }

    private Company createTestCompany() {
        Company company = new Company(
                "company1",
                "VAT",
                "address",
                "phone",
                "email");

        companyRepository.saveAndFlush(company);
        return companyRepository.findByName("company1").orElse(null);
    }

    private Part createTestPart(Brand brand, String partCode) {
        Part part = new Part(
                "UUID1",
                partCode,
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

        partRepository.saveAndFlush(part);
        return partRepository.findByPartCode(partCode).orElse(null);
    }

    private UserEntity createTestUser(Company company, String email, Map<Part, Integer> cart){
        UserEntity user = new UserEntity(
                "someUUID",
                email,
                "password",
                "Test",
                "User",
                "0888888888",
                UserRole.USER,
                cart,
                new ArrayList<>(),
                company);

        userRepository.saveAndFlush(user);
        return userRepository.findByEmail(email).orElse(null);
    }


    private Order createAwaitingOrder(UserEntity user, String address, String notes) {
        return new Order(
                user.getCart(),
                user,
                address,
                LocalDateTime.of(2024, 4, 12, 12, 35),
                null,
                null,
                notes);
    }

}

package bg.magna.websop.controller;


import bg.magna.websop.model.entity.Brand;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.repository.BrandRepository;
import bg.magna.websop.repository.OrderRepository;
import bg.magna.websop.repository.PartRepository;
import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PartsControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @AfterEach
    public void tearDown() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        partRepository.deleteAll();
        brandRepository.deleteAll();

        userService.addAdminUser();
        userService.addFirstUser();
    }

    @Test
    public void testViewAllParts() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();
        Part part1 = createTestPart(testBrand, "partCode1");
        Part part2 = createTestPart(testBrand, "partCode2");
        partRepository.saveAllAndFlush(List.of(part1, part2));

        mockMvc.perform(get("/parts/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("spare_parts"))
                .andExpect(model().attributeExists("parts"));
    }

    @Test
    public void testViewAddPart() throws Exception {
        mockMvc.perform(get("/parts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-part"))
                .andExpect(model().attributeExists("allBrands"));
    }

    @Test
    public void testAddPart() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();
        Part part1 = createTestPart(testBrand, "partCode1");

        Assertions.assertEquals(0, partRepository.count());

        mockMvc.perform(post("/parts/add")
                        .with(csrf())
                        .param("partCode", part1.getPartCode())
                        .param("descriptionEn", part1.getDescriptionEn())
                        .param("descriptionBg", part1.getDescriptionBg())
                        .param("imageURL", part1.getImageURL())
                        .param("brandName", part1.getBrand().getName())
                        .param("price", part1.getPrice().toString())
                        .param("size", part1.getSize())
                        .param("weight", String.valueOf(part1.getWeight()))
                        .param("suitableFor", part1.getSuitableFor())
                        .param("moreInfo", part1.getMoreInfo()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-panel"));

        Assertions.assertEquals(1, partRepository.count());
        Part actual = partRepository.findByPartCode(part1.getPartCode()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(part1.getPartCode(), actual.getPartCode());
        Assertions.assertEquals(part1.getPrice(), actual.getPrice());
        Assertions.assertEquals(0, actual.getQuantity());
        Assertions.assertEquals(part1.getBrand().getName(), actual.getBrand().getName());
        Assertions.assertEquals(part1.getDescriptionEn(), actual.getDescriptionEn());
        Assertions.assertEquals(part1.getDescriptionBg(), actual.getDescriptionBg());
        Assertions.assertEquals(part1.getImageURL(), actual.getImageURL());
        Assertions.assertEquals(part1.getSize(), actual.getSize());
        Assertions.assertEquals(part1.getSuitableFor(), actual.getSuitableFor());
        Assertions.assertEquals(part1.getWeight(), actual.getWeight());
        Assertions.assertEquals(part1.getMoreInfo(), actual.getMoreInfo());
    }

    @Test
    public void testAddPart_returnsIfInputIsInvalid() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();
        Part part1 = createTestPart(testBrand, "partCode1");

        Assertions.assertEquals(0, partRepository.count());

        mockMvc.perform(post("/parts/add")
                        .with(csrf())
                        .param("partCode", "")
                        .param("descriptionEn", part1.getDescriptionEn())
                        .param("descriptionBg", part1.getDescriptionBg())
                        .param("imageURL", part1.getImageURL())
                        .param("brandName", part1.getBrand().getName())
                        .param("price", part1.getPrice().toString())
                        .param("size", part1.getSize())
                        .param("weight", String.valueOf(part1.getWeight()))
                        .param("suitableFor", part1.getSuitableFor())
                        .param("moreInfo", part1.getMoreInfo()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts/add"));
    }

    @Test
    public void testAddPart_returnsIfPartCodeExists() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();

        Assertions.assertEquals(0, partRepository.count());

        Part part1 = createTestPartAndSaveToDB(testBrand, "partCode1");

        Assertions.assertEquals(1, partRepository.count());

        mockMvc.perform(post("/parts/add")
                        .with(csrf())
                        .param("partCode", part1.getPartCode())
                        .param("descriptionEn", part1.getDescriptionEn())
                        .param("descriptionBg", part1.getDescriptionBg())
                        .param("imageURL", part1.getImageURL())
                        .param("brandName", part1.getBrand().getName())
                        .param("price", part1.getPrice().toString())
                        .param("size", part1.getSize())
                        .param("weight", String.valueOf(part1.getWeight()))
                        .param("suitableFor", part1.getSuitableFor())
                        .param("moreInfo", part1.getMoreInfo()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts/add"))
                .andExpect(flash().attribute("partCodeExists", true));
    }

    @Test
    public void testViewPartDetails() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();
        Part part1 = createTestPartAndSaveToDB(testBrand, "partCode1");

        mockMvc.perform(get("/parts/" + part1.getPartCode()))
                .andExpect(status().isOk())
                .andExpect(view().name("part-details"))
                .andExpect(model().attributeExists("part"));
    }

    @Test
    @Transactional
    public void testAddPartToCart() throws Exception {

        Brand testBrand = createTestBrandAndSaveToDB();
        Part part1 = createTestPartAndSaveToDB(testBrand, "partCode1");
        UserEntity user = getUserRoleUser();

        Assertions.assertEquals(0, user.getCartSize());

        mockMvc.perform(post("/parts/add-to-cart/" + part1.getPartCode())
                        .with(user(user.getEmail()).roles("USER"))
                        .with(csrf())
                .param("quantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts/all"));

        UserEntity actualUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(1, actualUser.getCartSize());
        Assertions.assertEquals(5, actualUser.getCart().get(part1));

        orderRepository.deleteAll();
        userRepository.deleteAll();
        partRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testAddPartToCart_increasesPartQuantityWhenAddedSecondTime() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();
        Part part1 = createTestPartAndSaveToDB(testBrand, "partCode1");
        UserEntity user = getUserRoleUser();
        Map<Part, Integer> cart = new HashMap<>();
        cart.put(part1, 5);
        user.setCart(cart);
        userRepository.saveAndFlush(user);

        Assertions.assertEquals(1, user.getCartSize());


        mockMvc.perform(post("/parts/add-to-cart/" + part1.getPartCode())
                        .with(user(user.getEmail()).roles("USER"))
                        .with(csrf())
                        .param("quantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts/all"));

        UserEntity actualUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(1, actualUser.getCartSize());
        Assertions.assertEquals(10, actualUser.getCart().get(part1));

        orderRepository.deleteAll();
        userRepository.deleteAll();
        partRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @Test
    public void testViewEditPart() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();
        Part part1 = createTestPartAndSaveToDB(testBrand, "partCode1");

        mockMvc.perform(get("/parts/edit/" + part1.getPartCode())
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-part"))
                .andExpect(model().attributeExists("partData"))
                .andExpect(model().attributeExists("allBrands"));
    }

    @Test
    public void testEditPart() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();
        Part part1 = createTestPartAndSaveToDB(testBrand, "partCode1");


        mockMvc.perform(put("/parts/edit/" + part1.getPartCode())
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf())
                        .param("partCode", part1.getPartCode())
                        .param("descriptionEn", "newDescriptionEn")
                        .param("descriptionBg", "newDescriptionBg")
                        .param("imageURL", part1.getImageURL())
                        .param("brandName", part1.getBrand().getName())
                        .param("price", part1.getPrice().toString())
                        .param("quantity", "20")
                        .param("size", part1.getSize())
                        .param("weight", String.valueOf(part1.getWeight()))
                        .param("suitableFor", part1.getSuitableFor())
                        .param("moreInfo", part1.getMoreInfo()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts/" + part1.getPartCode()));

        Assertions.assertEquals(1, partRepository.count());
        Part actual = partRepository.findByPartCode(part1.getPartCode()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(part1.getPartCode(), actual.getPartCode());
        Assertions.assertEquals(part1.getPrice(), actual.getPrice());
        Assertions.assertEquals(20, actual.getQuantity());
        Assertions.assertEquals(part1.getBrand().getName(), actual.getBrand().getName());
        Assertions.assertEquals("newDescriptionEn", actual.getDescriptionEn());
        Assertions.assertEquals("newDescriptionBg", actual.getDescriptionBg());
        Assertions.assertEquals(part1.getImageURL(), actual.getImageURL());
        Assertions.assertEquals(part1.getSize(), actual.getSize());
        Assertions.assertEquals(part1.getSuitableFor(), actual.getSuitableFor());
        Assertions.assertEquals(part1.getWeight(), actual.getWeight());
        Assertions.assertEquals(part1.getMoreInfo(), actual.getMoreInfo());
    }

    @Test
    public void testEditPart_returnsWhenInputIsInvalid() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();
        Part part1 = createTestPartAndSaveToDB(testBrand, "partCode1");


        mockMvc.perform(put("/parts/edit/" + part1.getPartCode())
                                .with(user("admin@example.com").roles("ADMIN"))
                                .with(csrf())
                        .param("partCode", part1.getPartCode())
                        .param("descriptionEn", "newDescriptionEn")
                        .param("descriptionBg", "newDescriptionBg")
                        .param("imageURL", "")
                        .param("brandName", "")
                        .param("price", part1.getPrice().toString())
                        .param("quantity", "20")
                        .param("size", part1.getSize())
                        .param("weight", String.valueOf(part1.getWeight()))
                        .param("suitableFor", part1.getSuitableFor())
                        .param("moreInfo", part1.getMoreInfo()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts/edit/" + part1.getPartCode()))
                .andExpect(flash().attribute("fieldsHaveErrors", true));
    }

    @Test
    public void testDeletePart() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();

        Assertions.assertEquals(0, partRepository.count());

        Part part1 = createTestPartAndSaveToDB(testBrand, "partCode1");

        Assertions.assertEquals(1, partRepository.count());

        mockMvc.perform(delete("/parts/delete/" + part1.getPartCode())
                .with(user("admin@example.com").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts/all"));

        Assertions.assertEquals(0, partRepository.count());
    }

    @Test
    public void testDeletePart_doesNotDeletePartInExistingOrder() throws Exception {
        Brand testBrand = createTestBrandAndSaveToDB();

        Assertions.assertEquals(0, partRepository.count());
        Part part1 = createTestPartAndSaveToDB(testBrand, "partCode1");
        Assertions.assertEquals(1, partRepository.count());

        Map<Part, Integer> cart = Map.of(part1, 5);
        UserEntity user = userRepository.findByEmail("user01@example.com").orElse(null);
        Assertions.assertNotNull(user);
        user.setCart(cart);
        userRepository.saveAndFlush(user);

        Order order = new Order(cart, user, "address", LocalDateTime.of(2024, 5, 5, 12, 30), LocalDateTime.of(2024, 5, 7, 12, 30), null, "notes");
        orderRepository.saveAndFlush(order);

        mockMvc.perform(delete("/parts/delete/" + part1.getPartCode())
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts/all"))
                .andExpect(flash().attribute("errorPartCode", part1.getPartCode()));

        Assertions.assertEquals(1, partRepository.count());
    }

    private Brand createTestBrandAndSaveToDB() {
        Brand brand = new Brand("brand1", "https://example.com/exampleLogo.png");
        return brandRepository.saveAndFlush(brand);
    }

    private Part createTestPart(Brand brand, String partCode) {
        return new Part(
                "UUID1",
                partCode,
                20,
                "descriptionEn",
                "descriptionBg",
                "https://someUrl.com",
                brand,
                new BigDecimal("20.00"),
                "size",
                0,
                "moreInfo",
                "suitableFor");
    }

    private Part createTestPartAndSaveToDB(Brand brand, String partCode) {
        Part part = createTestPart(brand, partCode);
        return partRepository.saveAndFlush(part);

    }

    private UserEntity getUserRoleUser() {
        return userRepository.findByEmail("user01@example.com").orElse(null);
    }
}

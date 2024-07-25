package bg.magna.websop.controller;

import bg.magna.websop.model.entity.*;
import bg.magna.websop.repository.BrandRepository;
import bg.magna.websop.repository.OrderRepository;
import bg.magna.websop.repository.PartRepository;
import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private BrandRepository brandRepository;

    @AfterEach
    public void tearDown() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        partRepository.deleteAll();
        brandRepository.deleteAll();
    }

//    @Test
//    public void testViewAllOrders() throws Exception {
////        @AuthenticationPrincipal
//
//        mockMvc.perform(get("/orders/all")
//                        .with(user("admin@example.com").roles("ADMIN")))
//                .andExpect(status().isOk())
//                .andExpect(view().name("all-orders"))
//                .andExpect(model().attributeExists("userDetails"))
//                .andExpect(model().attributeExists("awaitingOrders"))
//                .andExpect(model().attributeExists("dispatchedOrders"))
//                .andExpect(model().attributeExists("deliveredOrders"));
//    }

    @Test
    public void testDispatchOrder() throws Exception {
        Brand brand = createTestBrand();
        Part part = createTestPart(brand, "partCode");
        UserEntity userRoleUser = userRepository.findByEmail("user01@example.com").orElse(null);

        Map<Part, Integer> cart = new HashMap<>();
        cart.put(part, 5);
        userRoleUser.setCart(cart);
        userRepository.saveAndFlush(userRoleUser);

        Order order = createAwaitingOrderAndSaveToDB(userRoleUser, "address", "notes");

        Assertions.assertEquals(1, orderRepository.findAllByDispatchedOnNull().size());
        Assertions.assertEquals(0, orderRepository.findAllByDispatchedOnNotNullAndDeliveredOnNull().size());

        mockMvc.perform(post("/orders/dispatch/" + order.getId())
                .with(user("admin@example.com").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/all"));

        Assertions.assertEquals(0, orderRepository.findAllByDispatchedOnNull().size());
        Assertions.assertEquals(1, orderRepository.findAllByDispatchedOnNotNullAndDeliveredOnNull().size());
        Assertions.assertNotNull(orderRepository.findById(order.getId()).orElse(null).getDispatchedOn());
    }

    @Test
    public void testDispatchOrder_doesNotWorkIfNotAdmin() throws Exception {
        Brand brand = createTestBrand();
        Part part = createTestPart(brand, "partCode");
        UserEntity userRoleUser = userRepository.findByEmail("user01@example.com").orElse(null);

        Map<Part, Integer> cart = new HashMap<>();
        cart.put(part, 5);
        userRoleUser.setCart(cart);
        userRepository.saveAndFlush(userRoleUser);

        Order order = createAwaitingOrderAndSaveToDB(userRoleUser, "address", "notes");

        Assertions.assertEquals(1, orderRepository.findAllByDispatchedOnNull().size());
        Assertions.assertEquals(0, orderRepository.findAllByDispatchedOnNotNullAndDeliveredOnNull().size());

        mockMvc.perform(post("/orders/dispatch/" + order.getId())
                        .with(user("user01@example.com").roles("USER"))
                        .with(csrf()))
                .andExpect(status().isForbidden());

        Assertions.assertEquals(1, orderRepository.findAllByDispatchedOnNull().size());
        Assertions.assertEquals(0, orderRepository.findAllByDispatchedOnNotNullAndDeliveredOnNull().size());
    }

    @Test
    public void testDeliverOrder() throws Exception {
        Brand brand = createTestBrand();
        Part part = createTestPart(brand, "partCode");
        UserEntity userRoleUser = userRepository.findByEmail("user01@example.com").orElse(null);

        Map<Part, Integer> cart = new HashMap<>();
        cart.put(part, 5);
        userRoleUser.setCart(cart);
        userRepository.saveAndFlush(userRoleUser);

        Order order = createAwaitingOrderAndSaveToDB(userRoleUser, "address", "notes");
        orderService.dispatchOrder(order.getId());

        Assertions.assertEquals(1, orderRepository.findAllByDispatchedOnNotNullAndDeliveredOnNull().size());
        Assertions.assertEquals(0, orderRepository.findAllByDeliveredOnNotNull().size());

        mockMvc.perform(post("/orders/deliver/" + order.getId())
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/all"));

        Assertions.assertEquals(0, orderRepository.findAllByDispatchedOnNotNullAndDeliveredOnNull().size());
        Assertions.assertEquals(1, orderRepository.findAllByDeliveredOnNotNull().size());
        Assertions.assertNotNull(orderRepository.findById(order.getId()).orElse(null).getDeliveredOn());
    }

    @Test
    public void testDeliverOrder_doesNotWorkIfNotAdmin() throws Exception {
        Brand brand = createTestBrand();
        Part part = createTestPart(brand, "partCode");
        UserEntity userRoleUser = userRepository.findByEmail("user01@example.com").orElse(null);

        Map<Part, Integer> cart = new HashMap<>();
        cart.put(part, 5);
        userRoleUser.setCart(cart);
        userRepository.saveAndFlush(userRoleUser);

        Order order = createAwaitingOrderAndSaveToDB(userRoleUser, "address", "notes");
        orderService.dispatchOrder(order.getId());

        Assertions.assertEquals(1, orderRepository.findAllByDispatchedOnNotNullAndDeliveredOnNull().size());
        Assertions.assertEquals(0, orderRepository.findAllByDeliveredOnNotNull().size());

        mockMvc.perform(post("/orders/deliver/" + order.getId())
                        .with(user("user01@example.com").roles("USER"))
                        .with(csrf()))
                .andExpect(status().isForbidden());

        Assertions.assertEquals(1, orderRepository.findAllByDispatchedOnNotNullAndDeliveredOnNull().size());
        Assertions.assertEquals(0, orderRepository.findAllByDeliveredOnNotNull().size());
    }

//    @Test
//    public void testDeleteOrder() throws Exception {
////        @AuthenticationPrincipal
//
//        Brand brand = createTestBrand();
//        Part part = createTestPart(brand, "partCode");
//        UserEntity userRoleUser = userRepository.findByEmail("user01@example.com").orElse(null);
//
//        Map<Part, Integer> cart = new HashMap<>();
//        cart.put(part, 5);
//        userRoleUser.setCart(cart);
//        userRepository.saveAndFlush(userRoleUser);
//
//        Order order = createAwaitingOrderAndSaveToDB(userRoleUser, "address", "notes");
//
//        Assertions.assertEquals(1, orderRepository.count());
//
//        mockMvc.perform(delete("/orders/delete/" + order.getId())
//                        .with(user("admin@example.com").roles("ADMIN"))
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/orders/all"));
//
//        Assertions.assertEquals(0, orderRepository.count());
//    }

//    @Test
//    public void testViewOrderDetails() throws Exception {
////        @AuthenticationPrincipal
//
//        Brand brand = createTestBrand();
//        Part part = createTestPart(brand, "partCode");
//        UserEntity userRoleUser = userRepository.findByEmail("user01@example.com").orElse(null);
//
//        Map<Part, Integer> cart = new HashMap<>();
//        cart.put(part, 5);
//        userRoleUser.setCart(cart);
//        userRepository.saveAndFlush(userRoleUser);
//
//        Order order = createAwaitingOrderAndSaveToDB(userRoleUser, "address", "notes");
//
//        mockMvc.perform(get("/orders/" + order.getId())
//                        .with(user("admin@example.com").roles("ADMIN")))
//                .andExpect(status().isOk())
//                .andExpect(view().name("order-details"))
//                .andExpect(model().attributeExists("order"));
//    }

    private Brand createTestBrand() {
        Brand brand = new Brand("brand1", "https://example.com/exampleLogo.png");
        return brandRepository.saveAndFlush(brand);
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

        return partRepository.saveAndFlush(part);
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

    private Order createAwaitingOrderAndSaveToDB(UserEntity user, String address, String notes) {
        Order order = createAwaitingOrder(user, address, notes);
        return orderRepository.saveAndFlush(order);
    }
}

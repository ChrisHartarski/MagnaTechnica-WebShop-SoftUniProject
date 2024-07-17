package bg.magna.websop.service.impl;

import bg.magna.websop.model.entity.Company;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.OrderRepository;
import bg.magna.websop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    private OrderServiceImpl toTest;
    private static final Part TEST_PART_1 = new Part("TestPart1", 10);
    private static final Part TEST_PART_2 = new Part("TestPart2", 10);
    private final static Company TEST_COMPANY = new Company("TestCompany", "TestVAT", "TestAddress", "TestPhone", "testCompany@example.com");
    private final static UserEntity TEST_USER = new UserEntity("someUUID", "testUser@example.com", "password", "Test", "User", "0888888888", UserRole.USER, Map.of(TEST_PART_1, 2), new ArrayList<>(), TEST_COMPANY);
    private static final Order TEST_ORDER_AWAITING = new Order(1, Map.of(TEST_PART_1, 5), TEST_USER, "address", LocalDateTime.of(2024, 4, 12, 12, 35), null, null, "some notes");
    private static final Order TEST_ORDER_DISPATCHED = new Order(2, Map.of(TEST_PART_1, 5), TEST_USER, "address", LocalDateTime.of(2024, 3, 10, 10, 30), LocalDateTime.of(2024, 3, 12, 10, 30), null, "some notes");
    private static final Order TEST_ORDER_DELIVERED = new Order(3, Map.of(TEST_PART_1, 5), TEST_USER, "address", LocalDateTime.of(2024, 2, 5, 11, 20), LocalDateTime.of(2024, 2, 6, 10, 30), LocalDateTime.of(2024, 2, 7, 9, 45), "some notes");

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        toTest = new OrderServiceImpl(orderRepository, userService, new ModelMapper());
    }

    @Test
    void testPartIsInExistingOrder() {

    }
}

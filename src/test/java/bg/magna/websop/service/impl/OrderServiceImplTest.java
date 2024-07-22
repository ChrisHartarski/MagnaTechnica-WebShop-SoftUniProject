package bg.magna.websop.service.impl;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.dto.order.FullOrderDTO;
import bg.magna.websop.model.dto.order.OrderDataDTO;
import bg.magna.websop.model.dto.order.ShortOrderDTO;
import bg.magna.websop.model.entity.Company;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.OrderRepository;
import bg.magna.websop.service.UserService;
import bg.magna.websop.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    private OrderServiceImpl toTest;
    private static final Part TEST_PART_1 = new Part("TestPart1", BigDecimal.valueOf(20), 10);
    private static final Part TEST_PART_2 = new Part("TestPart2", BigDecimal.valueOf(20), 10);
    private static final Company TEST_COMPANY = new Company("TestCompany", "TestVAT", "TestAddress", "TestPhone", "testCompany@example.com");
    private static final UserEntity TEST_USER = new UserEntity("someUUID", "testUser@example.com", "password", "Test", "User", "0888888888", UserRole.USER, Map.of(TEST_PART_1, 5), new ArrayList<>(), TEST_COMPANY);
    private static final Order TEST_ORDER_AWAITING = new Order(1, TEST_USER.getCart(), TEST_USER, "address", LocalDateTime.of(2024, 4, 12, 12, 35), null, null, "some notes");
    private static final Order TEST_ORDER_DISPATCHED = new Order(2, TEST_USER.getCart(), TEST_USER, "address", LocalDateTime.of(2024, 3, 10, 10, 30), LocalDateTime.of(2024, 3, 12, 10, 30), null, "some notes");
    private static final Order TEST_ORDER_DELIVERED = new Order(3, TEST_USER.getCart(), TEST_USER, "address", LocalDateTime.of(2024, 2, 5, 11, 20), LocalDateTime.of(2024, 2, 6, 10, 30), LocalDateTime.of(2024, 2, 7, 9, 45), "some notes");
    private static final CurrentUserDetails TEST_CURRENT_USER_DETAILS = new CurrentUserDetails(TEST_USER.getEmail(), TEST_USER.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + TEST_USER.getUserRole())), TEST_USER.getId(), TEST_USER.getFirstName(), TEST_USER.getLastName(), TEST_USER.getCompany().getName(), TEST_USER.getCartSize());

    @Captor
    private ArgumentCaptor<Order> orderCaptor;
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
        when(orderRepository.findAll()).thenReturn(List.of(TEST_ORDER_AWAITING, TEST_ORDER_DISPATCHED, TEST_ORDER_DELIVERED));

        Assertions.assertTrue(toTest.partIsInExistingOrder(TEST_PART_1));
        Assertions.assertFalse(toTest.partIsInExistingOrder(TEST_PART_2));
    }

    @Test
    void testDeleteOrder() {
        when(orderRepository.findById(TEST_ORDER_AWAITING.getId())).thenReturn(Optional.of(TEST_ORDER_AWAITING));

        toTest.deleteOrder(TEST_ORDER_AWAITING.getId());

        verify(orderRepository).deleteById(TEST_ORDER_AWAITING.getId());
    }

    @Test
    void testDeleteOrder_throwsException() {
        when(orderRepository.findById(TEST_ORDER_AWAITING.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> toTest.deleteOrder(TEST_ORDER_AWAITING.getId()));
    }

    @Test
    void testDeleteOrder_doesNotDeleteDispatchedAndDelivered() {
        when(orderRepository.findById(TEST_ORDER_DISPATCHED.getId())).thenReturn(Optional.of(TEST_ORDER_DISPATCHED));
        when(orderRepository.findById(TEST_ORDER_DELIVERED.getId())).thenReturn(Optional.of(TEST_ORDER_DELIVERED));

        Assertions.assertFalse(toTest.deleteOrder(TEST_ORDER_DISPATCHED.getId()));
        Assertions.assertFalse(toTest.deleteOrder(TEST_ORDER_DELIVERED.getId()));
    }

    @Test
    void testCurrentUserOwnsOrder() {
        when(orderRepository.findById(TEST_ORDER_AWAITING.getId())).thenReturn(Optional.of(TEST_ORDER_AWAITING));

        Assertions.assertTrue(toTest.currentUserOwnsOrder(TEST_ORDER_AWAITING.getId(), TEST_CURRENT_USER_DETAILS));
    }

    @Test
    void testCurrentUserOwnsOrder_throwsException() {
        when(orderRepository.findById(TEST_ORDER_AWAITING.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,() -> toTest.currentUserOwnsOrder(TEST_ORDER_AWAITING.getId(), TEST_CURRENT_USER_DETAILS));
    }

    @Test
    void testAddOrder() {
        when(userService.getUserById(TEST_ORDER_AWAITING.getUser().getId())).thenReturn(TEST_ORDER_AWAITING.getUser());

        toTest.addOrder(new OrderDataDTO(TEST_ORDER_AWAITING.getDeliveryAddress(), TEST_ORDER_AWAITING.getNotes()),
                TEST_ORDER_AWAITING.getUser().getId());

        verify(orderRepository).saveAndFlush(orderCaptor.capture());
        Order actual = orderCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_ORDER_AWAITING.getPartsAndQuantities().size(), actual.getPartsAndQuantities().size());
        Assertions.assertEquals(TEST_ORDER_AWAITING.getPartsAndQuantities().get(TEST_PART_1), actual.getPartsAndQuantities().get(TEST_PART_1));
        Assertions.assertEquals(TEST_ORDER_AWAITING.getUser(), actual.getUser());
        Assertions.assertEquals(TEST_ORDER_AWAITING.getDeliveryAddress(), actual.getDeliveryAddress());
        Assertions.assertEquals(TEST_ORDER_AWAITING.getNotes(), actual.getNotes());
        Assertions.assertNotNull(actual.getCreatedOn());
        Assertions.assertNull(actual.getDispatchedOn());
        Assertions.assertNull(actual.getDeliveredOn());
    }

    @Test
    void testDispatchOrder() {
        when(orderRepository.findById(TEST_ORDER_AWAITING.getId())).thenReturn(Optional.of(TEST_ORDER_AWAITING));

        toTest.dispatchOrder(TEST_ORDER_AWAITING.getId());

        verify(orderRepository).saveAndFlush(orderCaptor.capture());
        Order actual = orderCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getCreatedOn());
        Assertions.assertNotNull(actual.getDispatchedOn());
        Assertions.assertNull(actual.getDeliveredOn());
    }

    @Test
    void testDispatchOrder_throwsException() {
        when(orderRepository.findById(TEST_ORDER_AWAITING.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,() -> toTest.dispatchOrder(TEST_ORDER_AWAITING.getId()));
    }

    @Test
    void testDeliverOrder() {
        when(orderRepository.findById(TEST_ORDER_DISPATCHED.getId())).thenReturn(Optional.of(TEST_ORDER_DISPATCHED));

        toTest.deliverOrder(TEST_ORDER_DISPATCHED.getId());

        verify(orderRepository).saveAndFlush(orderCaptor.capture());
        Order actual = orderCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getCreatedOn());
        Assertions.assertNotNull(actual.getDispatchedOn());
        Assertions.assertNotNull(actual.getDeliveredOn());
    }

    @Test
    void testDeliverOrder_throwsException() {
        when(orderRepository.findById(TEST_ORDER_DISPATCHED.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,() -> toTest.dispatchOrder(TEST_ORDER_DISPATCHED.getId()));
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(TEST_ORDER_DELIVERED.getId())).thenReturn(Optional.of(TEST_ORDER_DELIVERED));

        Order actual = toTest.getOrderById(TEST_ORDER_DELIVERED.getId());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_ORDER_DELIVERED.getId(), actual.getId());
        Assertions.assertEquals(TEST_ORDER_DELIVERED.getPartsAndQuantities().size(), actual.getPartsAndQuantities().size());
        Assertions.assertEquals(TEST_ORDER_DELIVERED.getPartsAndQuantities().get(TEST_PART_1), actual.getPartsAndQuantities().get(TEST_PART_1));
        Assertions.assertEquals(TEST_ORDER_DELIVERED.getUser(), actual.getUser());
        Assertions.assertEquals(TEST_ORDER_DELIVERED.getDeliveryAddress(), actual.getDeliveryAddress());
        Assertions.assertEquals(TEST_ORDER_DELIVERED.getNotes(), actual.getNotes());
        Assertions.assertEquals(TEST_ORDER_DELIVERED.getCreatedOn(), actual.getCreatedOn());
        Assertions.assertEquals(TEST_ORDER_DELIVERED.getDispatchedOn(), actual.getDispatchedOn());
        Assertions.assertEquals(TEST_ORDER_DELIVERED.getDeliveredOn(), actual.getDeliveredOn());
    }

    @Test
    void testGetOrderById_throwsException() {
        when(orderRepository.findById(TEST_ORDER_DELIVERED.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,() -> toTest.getOrderById(TEST_ORDER_DELIVERED.getId()));
    }


    @Test
    void testGetFullOrderDTO() {
        FullOrderDTO expected = new FullOrderDTO(TEST_ORDER_DELIVERED.getId(),
                TEST_ORDER_DELIVERED.getUser().getEmail(),
                TEST_ORDER_DELIVERED.getUser().getFullName(),
                TEST_ORDER_DELIVERED.getUser().getCompany().getName(),
                TEST_ORDER_DELIVERED.getPartsAndQuantities(),
                TEST_ORDER_DELIVERED.getCreatedOn(),
                TEST_ORDER_DELIVERED.getDispatchedOn(),
                TEST_ORDER_DELIVERED.getDeliveredOn(),
                TEST_ORDER_DELIVERED.getDeliveryAddress(),
                TEST_ORDER_DELIVERED.getNotes(),
                TEST_ORDER_DELIVERED.getTotalPrice());

        when(orderRepository.findById(TEST_ORDER_DELIVERED.getId())).thenReturn(Optional.of(TEST_ORDER_DELIVERED));

        FullOrderDTO actual = toTest.getFullOrderDTO(TEST_ORDER_DELIVERED.getId());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getUserEmail(), actual.getUserEmail());
        Assertions.assertEquals(expected.getUserFullName(), actual.getUserFullName());
        Assertions.assertEquals(expected.getUserCompanyName(), actual.getUserCompanyName());
        Assertions.assertEquals(expected.getPartsAndQuantities().size(), actual.getPartsAndQuantities().size());
        Assertions.assertEquals(expected.getPartsAndQuantities().get(TEST_PART_1), actual.getPartsAndQuantities().get(TEST_PART_1));
        Assertions.assertEquals(expected.getCreatedOn(), actual.getCreatedOn());
        Assertions.assertEquals(expected.getDispatchedOn(), actual.getDispatchedOn());
        Assertions.assertEquals(expected.getDeliveredOn(), actual.getDeliveredOn());
        Assertions.assertEquals(expected.getDeliveryAddress(), actual.getDeliveryAddress());
        Assertions.assertEquals(expected.getNotes(), actual.getNotes());
        Assertions.assertEquals(expected.getTotalPrice(), actual.getTotalPrice());
    }

    @Test
    void testGetFullOrderDTO_throwsException() {
        when(orderRepository.findById(TEST_ORDER_DELIVERED.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,() -> toTest.getFullOrderDTO(TEST_ORDER_DELIVERED.getId()));
    }

    @Test
    void testGetAllShortOrderDTOs() {
        ShortOrderDTO expectedAwaiting = getShortOrderDTOFromOrder(TEST_ORDER_AWAITING);
        ShortOrderDTO expectedDispatched = getShortOrderDTOFromOrder(TEST_ORDER_DISPATCHED);
        ShortOrderDTO expectedDelivered = getShortOrderDTOFromOrder(TEST_ORDER_DELIVERED);
        List<ShortOrderDTO> expected = List.of(expectedAwaiting, expectedDispatched, expectedDelivered);

        when(orderRepository.findAll()).thenReturn(List.of(TEST_ORDER_AWAITING, TEST_ORDER_DISPATCHED, TEST_ORDER_DELIVERED));

        List<ShortOrderDTO> actual = toTest.getAllShortOrderDTOs();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getId(), actual.get(0).getId());
        Assertions.assertEquals(expected.get(1).getId(), actual.get(1).getId());
        Assertions.assertEquals(expected.get(2).getId(), actual.get(2).getId());
    }

    @Test
    void testGetAllShortOrderDTOsByUser(){
        ShortOrderDTO expectedAwaiting = getShortOrderDTOFromOrder(TEST_ORDER_AWAITING);
        ShortOrderDTO expectedDispatched = getShortOrderDTOFromOrder(TEST_ORDER_DISPATCHED);
        ShortOrderDTO expectedDelivered = getShortOrderDTOFromOrder(TEST_ORDER_DELIVERED);
        List<ShortOrderDTO> expected = List.of(expectedAwaiting, expectedDispatched, expectedDelivered);

        when(orderRepository.findAllByUserId(TEST_USER.getId())).thenReturn(List.of(TEST_ORDER_AWAITING, TEST_ORDER_DISPATCHED, TEST_ORDER_DELIVERED));

        List<ShortOrderDTO> actual = toTest.getAllShortOrderDTOsByUser(TEST_USER.getId());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getId(), actual.get(0).getId());
        Assertions.assertEquals(expected.get(1).getId(), actual.get(1).getId());
        Assertions.assertEquals(expected.get(2).getId(), actual.get(2).getId());
    }

    private ShortOrderDTO getShortOrderDTOFromOrder(Order order) {
        return new ShortOrderDTO(
                order.getId(),
                order.getUser().getFullName(),
                order.getUser().getCompany().getName(),
                order.getCreatedOn(),
                order.getDispatchedOn(),
                order.getDeliveredOn(),
                order.getTotalPrice());
    }

}

package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.repository.OrderRepository;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.util.UserSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserSession userSession;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, UserSession userSession, UserService userService) {
        this.orderRepository = orderRepository;
        this.userSession = userSession;
        this.userService = userService;
    }

    @Override
    public void addOrder(OrderDataDTO orderData) {
        Order order = new Order();
        order.setPartsAndQuantities(userSession.getCart().getPartsAndQuantities());
        order.setUser(userService.getUserById(userSession.getId()));
        order.setDeliveryAddress(orderData.getDeliveryAddress());
        order.setCreatedOn(Instant.now());
        order.setNotes(orderData.getNotes());

        orderRepository.saveAndFlush(order);
    }

    @Override
    public List<Order> getAwaitingOrders() {
        return orderRepository.findAllByDispatchedOnNull();
    }

    @Override
    public List<Order> getDispatchedOrders() {
        return orderRepository.findAllByDispatchedOnNotNullAndDeliveredOnNull();
    }

    @Override
    public List<Order> getDeliveredOrders() {
        return orderRepository.findAllByDeliveredOnNotNull();
    }
}

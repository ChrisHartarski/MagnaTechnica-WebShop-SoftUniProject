package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.repository.OrderRepository;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.PartService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.util.UserSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserSession userSession;
    private final UserService userService;
    private final PartService partService;

    public OrderServiceImpl(OrderRepository orderRepository, UserSession userSession, UserService userService, PartService partService) {
        this.orderRepository = orderRepository;
        this.userSession = userSession;
        this.userService = userService;
        this.partService = partService;
    }

    @Override
    public void addOrder(OrderDataDTO orderData) {
        Order order = new Order();
        order.setPartsAndQuantities(createPartsAndQuantitiesMapFromCart(userSession.getCart().getPartsAndQuantities()));
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

    @Override
    public boolean partIsInExistingOrder(Part part) {
        return orderRepository.findAll().stream()
                .map(Order::getPartsAndQuantities)
                .map(Map::keySet).anyMatch(set -> set.contains(part));
    }

    private Map<Part,Integer> createPartsAndQuantitiesMapFromCart(Map<String,Integer> cartMap) {
        Map<Part, Integer> orderMap = new HashMap<>();
        cartMap.forEach((partCode, quantity) -> orderMap.put(partService.getPartByPartCode(partCode), quantity));
        return orderMap;
    }
}

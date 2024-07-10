package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.dto.ShortOrderDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.repository.OrderRepository;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.PartService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.util.UserSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, UserSession userSession, UserService userService, PartService partService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userSession = userSession;
        this.userService = userService;
        this.partService = partService;
        this.modelMapper = modelMapper;
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

    @Override
    public List<ShortOrderDTO> getAllShortOrderDTOs() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, ShortOrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void dispatchOrder(long id) {
        Order order = orderRepository.getReferenceById(id);
        order.setDispatchedOn(Instant.now());
        orderRepository.saveAndFlush(order);
    }

    @Override
    public void deliverOrder(long id) {
        Order order = orderRepository.getReferenceById(id);
        order.setDeliveredOn(Instant.now());
        orderRepository.saveAndFlush(order);
    }

    @Override
    public boolean deleteOrder(long id) {
        Order order = orderRepository.getReferenceById(id);

        if (order.getDispatchedOn() != null || order.getDeliveredOn() != null) {
            return false;
        }

        orderRepository.deleteById(id);
        return orderRepository.findById(id).isEmpty();
    }

    private Map<Part,Integer> createPartsAndQuantitiesMapFromCart(Map<String,Integer> cartMap) {
        Map<Part, Integer> orderMap = new HashMap<>();
        cartMap.forEach((partCode, quantity) -> orderMap.put(partService.getPartByPartCode(partCode), quantity));
        return orderMap;
    }
}

package bg.magna.websop.service.impl;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.dto.FullOrderDTO;
import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.dto.ShortOrderDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.repository.OrderRepository;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void addOrder(OrderDataDTO orderData, String userId) {
        Order order = new Order();
        fillOrderWithCartItems(order, userService.getUserById(userId).getCart());
        order.setUser(userService.getUserById(userId));
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
    public List<ShortOrderDTO> getAllShortOrderDTOsByUser(String userId) {
        return orderRepository.findAllByUserId(userId).stream()
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

    @Override
    public boolean currentUserOwnsOrder(long orderId, CurrentUserDetails userDetails) {
        String userId = orderRepository.getReferenceById(orderId).getUser().getId();
        return userId.equals(userDetails.getId());
    }

    @Override
    public FullOrderDTO getFullOrderDTO(long id) {
        Order order = orderRepository.getReferenceById(id);
        return modelMapper.map(order, FullOrderDTO.class);
    }

    private void fillOrderWithCartItems(Order order, Map<Part, Integer> cart) {
        cart.forEach((key, value) -> {
            order.getPartsAndQuantities().put(key, value);
        });
    }
}

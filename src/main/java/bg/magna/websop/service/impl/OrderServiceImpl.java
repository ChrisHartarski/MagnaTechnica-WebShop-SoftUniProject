package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.order.FullOrderDTO;
import bg.magna.websop.model.dto.order.OrderDataDTO;
import bg.magna.websop.model.dto.order.ShortOrderDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.repository.OrderRepository;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public boolean partIsInExistingOrder(Part part) {
        return orderRepository.findAll().stream()
                .map(Order::getPartsAndQuantities)
                .map(Map::keySet).anyMatch(set -> set.contains(part));
    }

    @Override
    public boolean deleteOrder(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getDispatchedOn() != null || order.getDeliveredOn() != null) {
            return false;
        }

        orderRepository.deleteById(id);
        return orderRepository.findById(id).isEmpty();
    }

    @Override
    public boolean currentUserOwnsOrder(long orderId, UserDetails userDetails) {
        String userEmail = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"))
                .getUser().getEmail();

        return userEmail.equals(userDetails.getUsername());
    }

    @Override
    @Transactional
    public void addOrder(OrderDataDTO orderData, String userId) {
        Order order = new Order();
        UserEntity user = userService.getUserById(userId);
        fillOrderWithCartItems(order, user.getCart());
        order.setUser(user);
        order.setDeliveryAddress(orderData.getDeliveryAddress());
        order.setCreatedOn(LocalDateTime.now());
        order.setNotes(orderData.getNotes());

        orderRepository.saveAndFlush(order);
    }

    @Override
    public void dispatchOrder(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setDispatchedOn(LocalDateTime.now());
        orderRepository.saveAndFlush(order);
    }

    @Override
    public void deliverOrder(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));;
        order.setDeliveredOn(LocalDateTime.now());
        orderRepository.saveAndFlush(order);
    }

    @Override
    public Order getOrderById(long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such order exists!"));
    }

    @Override
    public FullOrderDTO getFullOrderDTO(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such order exists!")) ;
        return modelMapper.map(order, FullOrderDTO.class);
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
    public List<ShortOrderDTO> getAllShortOrderDTOs() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, ShortOrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShortOrderDTO> getAllShortOrderDTOsByUser(String userEmail) {
        return orderRepository.findAllByUserEmail(userEmail).stream()
                .map(order -> modelMapper.map(order, ShortOrderDTO.class))
                .collect(Collectors.toList());
    }

    private void fillOrderWithCartItems(Order order, Map<Part, Integer> cart) {
        cart.forEach((key, value) -> {
            order.getPartsAndQuantities().put(key, value);
        });
    }
}

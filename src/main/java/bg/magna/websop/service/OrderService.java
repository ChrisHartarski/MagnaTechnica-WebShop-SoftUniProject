package bg.magna.websop.service;

import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;

import java.util.Collection;
import java.util.List;

public interface OrderService {
    void addOrder(OrderDataDTO orderData);

    List<Order> getAwaitingOrders();

    List<Order> getDispatchedOrders();

    List<Order> getDeliveredOrders();

    boolean partIsInExistingOrder(Part part);
}

package bg.magna.websop.service;

import bg.magna.websop.model.dto.order.FullOrderDTO;
import bg.magna.websop.model.dto.order.OrderDataDTO;
import bg.magna.websop.model.dto.order.ShortOrderDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {
    boolean partIsInExistingOrder(Part part);

    boolean deleteOrder(long id);

    boolean currentUserOwnsOrder(long orderId, UserDetails userDetails);

    void addOrder(OrderDataDTO orderData, String userId);

    void dispatchOrder(long id);

    void deliverOrder(long id);

    Order getOrderById(long id);

    FullOrderDTO getFullOrderDTO(long id);

    List<Order> getAwaitingOrders();

    List<Order> getDispatchedOrders();

    List<Order> getDeliveredOrders();

    List<ShortOrderDTO> getAllShortOrderDTOs();

    List<ShortOrderDTO> getAllShortOrderDTOsByUser(String userEmail);
}

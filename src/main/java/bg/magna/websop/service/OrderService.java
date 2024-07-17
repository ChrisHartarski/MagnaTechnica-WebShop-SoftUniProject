package bg.magna.websop.service;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.dto.FullOrderDTO;
import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.dto.ShortOrderDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;

import java.util.List;

public interface OrderService {
    boolean partIsInExistingOrder(Part part);

    boolean deleteOrder(long id);

    boolean currentUserOwnsOrder(long orderId, CurrentUserDetails userDetails);

    void addOrder(OrderDataDTO orderData, String userId);

    void dispatchOrder(long id);

    void deliverOrder(long id);

    Order getOrderById(long id);

    FullOrderDTO getFullOrderDTO(long id);

    List<Order> getAwaitingOrders();

    List<Order> getDispatchedOrders();

    List<Order> getDeliveredOrders();

    List<ShortOrderDTO> getAllShortOrderDTOs();

    List<ShortOrderDTO> getAllShortOrderDTOsByUser(String userId);
}

package bg.magna.websop.service;

import bg.magna.websop.model.dto.FullOrderDTO;
import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.dto.ShortOrderDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;

import java.util.List;

public interface OrderService {
    void addOrder(OrderDataDTO orderData);

    List<Order> getAwaitingOrders();

    List<Order> getDispatchedOrders();

    List<Order> getDeliveredOrders();

    boolean partIsInExistingOrder(Part part);

    List<ShortOrderDTO> getAllShortOrderDTOs();

    List<ShortOrderDTO> getAllShortOrderDTOsByUser(String userId);

    void dispatchOrder(long id);

    void deliverOrder(long id);
    boolean deleteOrder(long id);

    boolean currentUserOwnsOrder(long orderId);

    FullOrderDTO getFullOrderDTO(long id);
}

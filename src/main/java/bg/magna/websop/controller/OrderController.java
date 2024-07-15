package bg.magna.websop.controller;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.dto.FullOrderDTO;
import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.dto.ShortOrderDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.PartService;
import bg.magna.websop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final PartService partService;

    public OrderController(OrderService orderService, PartService partService) {
        this.orderService = orderService;
        this.partService = partService;
    }

    @ModelAttribute("orderData")
    public OrderDataDTO orderData() {
        return new OrderDataDTO();
    }

    @GetMapping("/all")
    public String viewAllOrders(Model model, @AuthenticationPrincipal CurrentUserDetails userDetails) {

        List<ShortOrderDTO> orders = new ArrayList<>();

        if(userDetails.isAdmin()) {
            orders = orderService.getAllShortOrderDTOs();
        } else {
            orders = orderService.getAllShortOrderDTOsByUser(userDetails.getId());
        }

        List<ShortOrderDTO> awaitingOrders = orders.stream()
                        .filter(order -> order.getDispatchedOn() == null)
                                .toList();
        List<ShortOrderDTO> dispatchedOrders = orders.stream()
                        .filter(order -> order.getDispatchedOn() != null
                                        && order.getDeliveredOn() == null)
                                .toList();
        List<ShortOrderDTO> deliveredOrders = orders.stream()
                        .filter(order -> order.getDeliveredOn() != null)
                                .toList();

        model.addAttribute("userDetails", userDetails);
        model.addAttribute("awaitingOrders", awaitingOrders);
        model.addAttribute("dispatchedOrders", dispatchedOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);

        return "all-orders";
    }

    @PostMapping("/dispatch/{id}")
    public String dispatchOrder(@PathVariable long id) {

        orderService.dispatchOrder(id);
        return "redirect:/orders/all";
    }

    @PostMapping("/deliver/{id}")
    public String deliverOrder(@PathVariable long id) {

        orderService.deliverOrder(id);
        return "redirect:/orders/all";
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public String deleteOrder(@PathVariable long id,
                              @AuthenticationPrincipal CurrentUserDetails userDetails,
                              RedirectAttributes redirectAttributes) {

        if (!userDetails.isAdmin() && !orderService.currentUserOwnsOrder(id, userDetails)) {
            return "redirect:/";
        }

        Order order = orderService.getOrderById(id);
        boolean orderDeleted = orderService.deleteOrder(id);
        if (orderDeleted) {
            order.getPartsAndQuantities().forEach(partService::increaseQuantity);
        } else {
            redirectAttributes.addFlashAttribute("alreadyDispatchedOrDelivered", true);
            redirectAttributes.addFlashAttribute("orderId", id);
        }

        return "redirect:/orders/all";
    }

    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable long id, @AuthenticationPrincipal CurrentUserDetails userDetails, Model model) {
        if (!userDetails.isAdmin() && !orderService.currentUserOwnsOrder(id, userDetails)){
            return "redirect:/";
        }

        FullOrderDTO order = orderService.getFullOrderDTO(id);
        model.addAttribute("order", order);

        return "order-details";
    }
}

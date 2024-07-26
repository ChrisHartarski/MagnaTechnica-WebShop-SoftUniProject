package bg.magna.websop.controller;

import bg.magna.websop.model.dto.order.FullOrderDTO;
import bg.magna.websop.model.dto.order.OrderDataDTO;
import bg.magna.websop.model.dto.order.ShortOrderDTO;
import bg.magna.websop.model.entity.Order;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.PartService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
    public String viewAllOrders(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        List<ShortOrderDTO> orders = new ArrayList<>();

        if(currentUserIsAdmin(userDetails)) {
            orders = orderService.getAllShortOrderDTOs();
        } else {
            orders = orderService.getAllShortOrderDTOsByUser(userDetails.getUsername());
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

    @Transactional
    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable long id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {

        if (!currentUserIsAdmin(userDetails) && !orderService.currentUserOwnsOrder(id, userDetails)) {
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
    public String viewOrderDetails(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!currentUserIsAdmin(userDetails) && !orderService.currentUserOwnsOrder(id, userDetails)){
            return "redirect:/";
        }

        FullOrderDTO order = orderService.getFullOrderDTO(id);
        model.addAttribute("order", order);

        return "order-details";
    }

    private boolean currentUserIsAdmin(UserDetails userDetails) {
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return authorities.contains("ROLE_ADMIN");
    }
}

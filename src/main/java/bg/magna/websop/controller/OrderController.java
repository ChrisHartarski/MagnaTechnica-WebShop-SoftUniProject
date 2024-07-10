package bg.magna.websop.controller;

import bg.magna.websop.model.dto.ShortOrderDTO;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.util.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserSession userSession;

    public OrderController(OrderService orderService, UserSession userSession) {
        this.orderService = orderService;
        this.userSession = userSession;
    }

    @GetMapping("/all")
    public String viewAllOrders(Model model) {
        if(!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        List<ShortOrderDTO> orders = orderService.getAllShortOrderDTOs();
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

        model.addAttribute("awaitingOrders", awaitingOrders);
        model.addAttribute("dispatchedOrders", dispatchedOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);

        return "all-orders";
    }

    @PostMapping("/dispatch/{id}")
    public String dispatchOrder(@PathVariable long id) {
        if (!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        orderService.dispatchOrder(id);
        return "redirect:/orders/all";
    }

    @PostMapping("/deliver/{id}")
    public String deliverOrder(@PathVariable long id) {
        if (!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        orderService.deliverOrder(id);
        return "redirect:/orders/all";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable long id, RedirectAttributes redirectAttributes) {
        if (!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        boolean orderDeleted = orderService.deleteOrder(id);
        if (!orderDeleted) {
            redirectAttributes.addFlashAttribute("alreadyDispatchedOrDelivered", true);
            redirectAttributes.addFlashAttribute("orderId", id);
        }
        return "redirect:/orders/all";
    }
}

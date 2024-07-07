package bg.magna.websop.controller;

import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.PartService;
import bg.magna.websop.util.UserSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CartController {
    private final UserSession userSession;
    private final PartService partService;
    private final OrderService orderService;


    public CartController(UserSession userSession, PartService partService, OrderService orderService) {
        this.userSession = userSession;
        this.partService = partService;
        this.orderService = orderService;
    }

    @ModelAttribute("orderData")
    public OrderDataDTO orderDataDTO() {
        return new OrderDataDTO();
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        if (!userSession.isUserWithUserRoleLoggedIn()) {
            return "redirect:/";
        }

        Map<String, Part> cartPartsMap = partService.createCartPartsMap();
        BigDecimal cartTotal = partService.getCartTotalPrice();


        model.addAttribute("cartPartsMap", cartPartsMap);
        model.addAttribute("cartTotal", cartTotal);


        return "cart";
    }

    @DeleteMapping("/cart/remove-item/{partCode}")
    public String deletePartFromCart(@PathVariable String partCode) {
        if (!userSession.isUserWithUserRoleLoggedIn()) {
            return "redirect:/";
        }

        userSession.getCart().getPartsAndQuantities().remove(partCode);

        return "redirect:/cart";
    }

    @PostMapping("/orders/add")
    public String addOrder(@Valid OrderDataDTO orderData,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (!userSession.isUserWithUserRoleLoggedIn()) {
            return "redirect:/";
        }

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("orderData", orderData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderData", bindingResult);
            return "redirect:/cart";
        }

        orderService.addOrder(orderData);
        partService.removeQuantitiesFromParts(userSession.getCart().getPartsAndQuantities());
        userSession.getCart().setPartsAndQuantities(new HashMap<>());

        return "redirect:/web-shop";
    }
}

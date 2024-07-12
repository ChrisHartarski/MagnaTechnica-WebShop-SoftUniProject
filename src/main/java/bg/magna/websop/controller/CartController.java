package bg.magna.websop.controller;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.PartService;
import bg.magna.websop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Map;

@Controller
public class CartController {
    private final UserService userService;
    private final PartService partService;
    private final OrderService orderService;


    public CartController(UserService userService, PartService partService, OrderService orderService) {
        this.userService = userService;
        this.partService = partService;
        this.orderService = orderService;
    }

    @ModelAttribute("orderData")
    public OrderDataDTO orderDataDTO() {
        return new OrderDataDTO();
    }

    @GetMapping("/cart")
    public String viewCart(Model model, @AuthenticationPrincipal CurrentUserDetails userDetails) {

        UserEntity user = userService.getUserById(userDetails.getId());

        String userFullName = user.getFullName();
        String userEmail = user.getEmail();
        Map<Part, Integer> cart = user.getCart();
        BigDecimal cartTotal = partService.getCartTotalPrice(userDetails.getId());

        model.addAttribute("userFullName", userFullName);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cartTotal);


        return "cart";
    }

    @DeleteMapping("/cart/remove-item/{partCode}")
    public String deletePartFromCart(@PathVariable String partCode, @AuthenticationPrincipal CurrentUserDetails userDetails) {
        UserEntity user = userService.getUserById(userDetails.getId());
        user.getCart().remove(partService.getPartByPartCode(partCode));
        userService.saveUserToDB(user);

        return "redirect:/cart";
    }

    @PostMapping("/orders/add")
    public String addOrder(@Valid OrderDataDTO orderData,
                           @AuthenticationPrincipal CurrentUserDetails userDetails,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("orderData", orderData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderData", bindingResult);
            return "redirect:/cart";
        }

        orderService.addOrder(orderData, userDetails.getId());
        UserEntity user = userService.getUserById(userDetails.getId());
        partService.removeQuantitiesFromParts(user.getCart());
        userService.emptyUserCart(user);

        return "redirect:/web-shop";
    }
}

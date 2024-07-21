package bg.magna.websop.controller;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.dto.order.OrderDataDTO;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.PartService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.service.helper.UserHelperService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {
    private final UserService userService;
    private final PartService partService;
    private final OrderService orderService;
    private final UserHelperService userHelperService;


    public CartController(UserService userService, PartService partService, OrderService orderService, UserHelperService userHelperService) {
        this.userService = userService;
        this.partService = partService;
        this.orderService = orderService;
        this.userHelperService = userHelperService;
    }

    @ModelAttribute("orderData")
    public OrderDataDTO orderData() {
        return new OrderDataDTO();
    }

    @GetMapping("/cart")
    public String viewCart() {

        return "cart";
    }

    @PostMapping("/cart/add-order")
    public String addOrder(@Valid OrderDataDTO orderData,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderData", bindingResult);
            redirectAttributes.addFlashAttribute("orderData", orderData);
            return "redirect:/cart";
        }

        UserEntity user = userService.getUserById(userHelperService.getCurrentUserDetails().getId());

        if(user.getCart().isEmpty()) {
            redirectAttributes.addFlashAttribute("cartEmpty", true);
            return "redirect:/cart";
        }

        orderService.addOrder(orderData, user.getId());
        partService.removeQuantitiesFromParts(user.getCart());
        userService.emptyUserCart(user);

        return "redirect:/";
    }

    @DeleteMapping("/cart/remove-item/{partCode}")
    public String deletePartFromCart(@PathVariable String partCode, @AuthenticationPrincipal CurrentUserDetails userDetails) {
        UserEntity user = userService.getUserById(userDetails.getId());
        user.getCart().remove(partService.getPartByPartCode(partCode));
        userService.saveUserToDB(user);

        return "redirect:/cart";
    }
}

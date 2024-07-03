package bg.magna.websop.controller;

import bg.magna.websop.model.dto.OrderDataDTO;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.service.PartService;
import bg.magna.websop.util.Cart;
import bg.magna.websop.util.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final UserSession userSession;
    private final PartService partService;


    public CartController(UserSession userSession, PartService partService) {
        this.userSession = userSession;
        this.partService = partService;
    }

    @ModelAttribute("orderData")
    public OrderDataDTO orderDataDTO() {
        return new OrderDataDTO();
    }

    @GetMapping("")
    public String viewCart() {
        if (!userSession.isUserWithUserRoleLoggedIn()) {
            return "redirect:/";
        }
        return "cart";
    }

    @DeleteMapping("/delete-item/{partCode}")
    public String deletePartFromCart(@PathVariable String partCode) {
        if (!userSession.isUserWithUserRoleLoggedIn()) {
            return "redirect:/";
        }

        Part part = partService.getPartByPartCode(partCode);
        userSession.getCart().getPartsAndQuantities().remove(part);

        return "redirect:/cart";
    }
}

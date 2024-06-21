package bg.magna.websop.controller;

import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.PartService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.util.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelController {
    private final UserService userService;
    private final BrandService brandService;
    private final PartService partService;
    private final UserSession userSession;

    public AdminPanelController(UserService userService, BrandService brandService, PartService partService, UserSession userSession) {
        this.userService = userService;
        this.brandService = brandService;
        this.partService = partService;
        this.userSession = userSession;
    }

    @GetMapping("/admin-panel")
    public String viewAdminPanel(Model model) {
        if(!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        model.addAttribute("usersCount", userService.getUserCount());
        model.addAttribute("adminRolesCount", userService.getUserCountByRole(UserRole.ADMIN));
        model.addAttribute("userRolesCount", userService.getUserCountByRole(UserRole.USER));

        model.addAttribute("brandsCount", brandService.getCount());
        model.addAttribute("partArticlesCount", partService.getCount());
        model.addAttribute("totalPartsCount", partService.getTotalParts());

        return "admin-panel";
    }
}

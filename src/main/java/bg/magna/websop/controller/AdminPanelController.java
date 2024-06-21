package bg.magna.websop.controller;

import bg.magna.websop.model.dto.AddBrandDTO;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.PartService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.util.UserSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @ModelAttribute("brandData")
    public AddBrandDTO addBrandDTO() {
        return new AddBrandDTO();
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

    @GetMapping("/add-brand")
    public String viewAddBrand() {
        if(!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }
        return "add-brand";
    }

    @PostMapping("/add-brand")
    public String addBrand(@Valid AddBrandDTO brandData,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if(!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("brandData", brandData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.brandData", bindingResult);
            return "redirect:/add-brand";
        }

        if(brandService.brandExists(brandData.getName())) {
            redirectAttributes.addFlashAttribute("brandData", brandData);
            redirectAttributes.addFlashAttribute("brandExists", true);
            return "redirect:/add-brand";
        }

        brandService.addBrand(brandData.getName());
        return "redirect:/admin-panel";
    }
}

package bg.magna.websop.controller;

import bg.magna.websop.model.dto.AddBrandDTO;
import bg.magna.websop.model.dto.FullEnquiryDTO;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class AdminPanelController {
    private final UserService userService;
    private final BrandService brandService;
    private final PartService partService;
    private final OrderService orderService;
    private final MachineService machineService;
    private final EnquiryService enquiryService;

    public AdminPanelController(UserService userService, BrandService brandService, PartService partService, OrderService orderService, MachineService machineService, EnquiryService enquiryService) {
        this.userService = userService;
        this.brandService = brandService;
        this.partService = partService;
        this.orderService = orderService;
        this.machineService = machineService;
        this.enquiryService = enquiryService;
    }

    @ModelAttribute("brandData")
    public AddBrandDTO addBrandDTO() {
        return new AddBrandDTO();
    }

    @GetMapping("/admin-panel")
    public String viewAdminPanel(Model model) {

        model.addAttribute("usersCount", userService.getUserCount());
        model.addAttribute("adminRolesCount", userService.getUserCountByRole(UserRole.ADMIN));
        model.addAttribute("userRolesCount", userService.getUserCountByRole(UserRole.USER));

        model.addAttribute("brandsCount", brandService.getCount());
        model.addAttribute("partArticlesCount", partService.getCount());
        model.addAttribute("totalPartsCount", partService.getTotalParts());

        model.addAttribute("awaitingOrdersCount", orderService.getAwaitingOrders().size());
        model.addAttribute("dispatchedOrdersCount", orderService.getDispatchedOrders().size());
        model.addAttribute("deliveredOrdersCount", orderService.getDeliveredOrders().size());

        return "admin-panel";
    }

    @GetMapping("/brands/add")
    public String viewAddBrand() {

        return "add-brand";
    }

    @PostMapping("/brands/add")
    public String addBrand(@Valid AddBrandDTO brandData,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("brandData", brandData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.brandData", bindingResult);
            return "redirect:/brands/add";
        }

        if(brandService.brandExists(brandData.getName())) {
            redirectAttributes.addFlashAttribute("brandData", brandData);
            redirectAttributes.addFlashAttribute("brandExists", true);
            return "redirect:/brands/add";
        }

        brandService.addBrand(brandData.getName(), brandData.getLogoURL());
        return "redirect:/admin-panel";
    }

    @PostMapping("/admin-panel/initializeMockDB")
    public String initializeMockDB() throws IOException {
        brandService.initializeMockBrands();
        partService.initializeMockParts();
        machineService.initializeMockMachines();
        return "redirect:/";
    }
}

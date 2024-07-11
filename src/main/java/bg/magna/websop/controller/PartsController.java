package bg.magna.websop.controller;

import bg.magna.websop.model.dto.PartDataDTO;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.PartService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/parts")
public class PartsController {
    private final UserSession userSession;
    private final BrandService brandService;
    private final PartService partService;
    private final OrderService orderService;

    public PartsController(UserSession userSession, BrandService brandService, PartService partService, OrderService orderService) {
        this.userSession = userSession;
        this.brandService = brandService;
        this.partService = partService;
        this.orderService = orderService;
    }

    @ModelAttribute("partData")
    public PartDataDTO addPartDTO() {
        return new PartDataDTO();
    }

    @GetMapping("/add")
    public String viewAddPart(Model model) {
        if(!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        model.addAttribute("allBrands", brandService.getAllBrandNames());

        return "add-part";
    }

    @PostMapping("/add")
    public String addPart(@Valid PartDataDTO partData,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if(!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("partData", partData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.partData", bindingResult);
            return "redirect:/parts/add";
        }

        if(partService.partExists(partData.getPartCode())) {
            redirectAttributes.addFlashAttribute("partData", partData);
            redirectAttributes.addFlashAttribute("partCodeExists", true);
            return "redirect:/parts/add";
        }

        partService.addPart(partData);
        return "redirect:/admin-panel";
    }

    @GetMapping("/{partCode}")
    public String getPartDetails(@PathVariable("partCode") String partCode, Model model) {

        PartDataDTO part = partService.getPartDTOFromPartCode(partCode);
        model.addAttribute("part", part);

        return "part-details";
    }

    @PostMapping("/add-to-cart/{partCode}")
    public String addPartToCart(@PathVariable String partCode, @RequestParam Integer quantity) {
        Cart cart = userSession.getCart();

        if (cart.getPartsAndQuantities().containsKey(partCode)) {
            cart.getPartsAndQuantities().put(partCode, cart.getPartsAndQuantities().get(partCode) + quantity);
        } else {
            cart.getPartsAndQuantities().put(partCode, quantity);
        }

        return "redirect:/web-shop";
    }

    @GetMapping("/edit/{partCode}")
    public String viewEditPart(@PathVariable String partCode, Model model) {
        if(!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        PartDataDTO partDataDTO = partService.getPartDTOFromPartCode(partCode);

        model.addAttribute("partData", partDataDTO);
        model.addAttribute("allBrands", brandService.getAllBrandNames());

        return "edit-part";
    }

    @PostMapping("/edit/{partCode}")
    public String editPart(@PathVariable String partCode,
                          @Valid PartDataDTO partData,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if(!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("partData", partData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.partData", bindingResult);
            return "redirect:/parts/edit/{partCode}";
        }

        partService.editPart(partData);

        return "redirect:/admin-panel";
    }

    @DeleteMapping("/delete/{partCode}")
    public String deletePart(@PathVariable String partCode,
                             RedirectAttributes redirectAttributes) {
        if(!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }

        if (orderService.partIsInExistingOrder(partService.getPartByPartCode(partCode))) {
            redirectAttributes.addFlashAttribute("partInExistingOrder", true);
            redirectAttributes.addFlashAttribute("errorPartCode", partCode);
            return "redirect:/web-shop";
        }

        partService.deletePart(partCode);
        return "redirect:/web-shop";
    }
}

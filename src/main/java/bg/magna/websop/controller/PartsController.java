package bg.magna.websop.controller;

import bg.magna.websop.model.dto.part.PartDataDTO;
import bg.magna.websop.model.dto.part.ShortPartDataDTO;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.OrderService;
import bg.magna.websop.service.PartService;
import bg.magna.websop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/parts")
public class PartsController {
    private final UserService userService;
    private final BrandService brandService;
    private final PartService partService;
    private final OrderService orderService;

    public PartsController(UserService userService, BrandService brandService, PartService partService, OrderService orderService) {
        this.userService = userService;
        this.brandService = brandService;
        this.partService = partService;
        this.orderService = orderService;
    }

    @ModelAttribute("partData")
    public PartDataDTO addPartDTO() {
        return new PartDataDTO();
    }

    @GetMapping("/all")
    public String viewWebShop(Model model) {

        List<ShortPartDataDTO> parts = partService.getAllShortPartDTOs();
        model.addAttribute("parts", parts);

        return "spare_parts";
    }

    @GetMapping("/add")
    public String viewAddPart(Model model) {

        model.addAttribute("allBrands", brandService.getAllBrandNames());

        return "add-part";
    }

    @PostMapping("/add")
    public String addPart(@Valid PartDataDTO partData,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

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
    public String viewPartDetails(@PathVariable("partCode") String partCode, Model model) {

        PartDataDTO part = partService.getPartDTOFromPartCode(partCode);
        model.addAttribute("part", part);

        return "part-details";
    }

    @PostMapping("/add-to-cart/{partCode}")
    public String addPartToCart(@PathVariable String partCode, @RequestParam Integer quantity, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getUserByEmail(userDetails.getUsername());

        if (user.getCart().containsKey(partService.getPartByPartCode(partCode))) {
            user.getCart().put(partService.getPartByPartCode(partCode), user.getCart().get(partService.getPartByPartCode(partCode)) + quantity);
        } else {
            user.getCart().put(partService.getPartByPartCode(partCode), quantity);
        }

        userService.saveUserToDB(user);

        return "redirect:/parts/all";
    }

    @GetMapping("/edit/{partCode}")
    public String viewEditPart(@PathVariable String partCode, Model model) {

        PartDataDTO partDataDTO = partService.getPartDTOFromPartCode(partCode);

        model.addAttribute("partData", partDataDTO);
        model.addAttribute("allBrands", brandService.getAllBrandNames());

        return "edit-part";
    }

    @PutMapping("/edit/{partCode}")
    public String editPart(@PathVariable String partCode,
                          @Valid PartDataDTO partData,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("partData", partData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.partData", bindingResult);
            redirectAttributes.addFlashAttribute("fieldsHaveErrors", true);
            return "redirect:/parts/edit/{partCode}";
        }

        partService.editPart(partData);

        return "redirect:/parts/all";
    }

    @DeleteMapping("/delete/{partCode}")
    public String deletePart(@PathVariable String partCode,
                             RedirectAttributes redirectAttributes) {

        if (orderService.partIsInExistingOrder(partService.getPartByPartCode(partCode))) {
            redirectAttributes.addFlashAttribute("partInExistingOrder", true);
            redirectAttributes.addFlashAttribute("errorPartCode", partCode);
            return "redirect:/parts/all";
        }

        partService.deletePart(partCode);
        return "redirect:/parts/all";
    }
}

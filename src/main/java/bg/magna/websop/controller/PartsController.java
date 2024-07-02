package bg.magna.websop.controller;

import bg.magna.websop.model.dto.AddPartDTO;
import bg.magna.websop.model.dto.FullPartDTO;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.PartService;
import bg.magna.websop.util.UserSession;
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

    public PartsController(UserSession userSession, BrandService brandService, PartService partService) {
        this.userSession = userSession;
        this.brandService = brandService;
        this.partService = partService;
    }

    @ModelAttribute("partData")
    public AddPartDTO addPartDTO() {
        return new AddPartDTO();
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
    public String addPart(@Valid AddPartDTO partData,
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
        FullPartDTO part = partService.getPartDTOFromPartCode(partCode);
        model.addAttribute("part", part);

        return "part-details";
    }
}

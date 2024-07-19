package bg.magna.websop.controller;

import bg.magna.websop.model.dto.ShortPartDataDTO;
import bg.magna.websop.service.PartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final PartService partService;

    public HomeController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping("/")
    public String viewIndex() {
        return "index";
    }

    @GetMapping("/spare-parts")
    public String viewWebShop(Model model) {

        List<ShortPartDataDTO> parts = partService.getAllShortPartDTOs();
        model.addAttribute("parts", parts);

        return "spare_parts";
    }

    @GetMapping("/contact")
    public String viewContact() {
        return "contact";
    }

}

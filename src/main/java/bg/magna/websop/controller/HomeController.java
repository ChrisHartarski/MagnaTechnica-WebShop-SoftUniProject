package bg.magna.websop.controller;

import bg.magna.websop.model.dto.ShortPartDataDTO;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.PartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private PartService partService;

    public HomeController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping("/")
    public String viewIndex() {
        return "index";
    }

    @GetMapping("/web-shop")
    public String viewWebShop(Model model) {

        List<ShortPartDataDTO> parts = partService.getAllShortPartDTOs();
        model.addAttribute("parts", parts);

        return "web-shop";
    }

    @GetMapping("/contact")
    public String viewContact() {
        return "contact";
    }

}

package bg.magna.websop.controller;

import bg.magna.websop.model.entity.Part;
import bg.magna.websop.service.PartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class WebShopController {
    private final PartService partService;

    public WebShopController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping("/web-shop")
    public String viewWebShop(Model model) {

        List<Part> parts = partService.getAllParts();
        model.addAttribute("parts", parts);

        return "web-shop";
    }
}

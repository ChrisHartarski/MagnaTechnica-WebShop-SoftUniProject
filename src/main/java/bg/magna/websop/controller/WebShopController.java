package bg.magna.websop.controller;

import bg.magna.websop.model.entity.Part;
import bg.magna.websop.service.PartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class WebShopController {
    private final PartService partService;

    public WebShopController(PartService partService) {
        this.partService = partService;
    }


    @ModelAttribute("parts")
    public List<Part> parts() {
        return partService.getAllParts();
    }

    @GetMapping("/web-shop")
    public String viewWebShop() {
        return "web-shop";
    }
}

package bg.magna.websop.controller;

import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.PartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final BrandService brandService;
    private final PartService partService;

    public HomeController(BrandService brandService, PartService partService) {
        this.brandService = brandService;
        this.partService = partService;
    }

    @GetMapping("/")
    public String viewIndex() {
        return "index";
    }

    @GetMapping("/about")
    public String viewAbout() {
        return "about";
    }

}

package bg.magna.websop.controller;

import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.PartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String viewIndex() {
        return "index";
    }

}

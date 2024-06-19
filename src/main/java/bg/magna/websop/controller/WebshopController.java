package bg.magna.websop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebshopController {

    @GetMapping("/webshop")
    public String viewWebshop() {
        return "/webshop";
    }
}

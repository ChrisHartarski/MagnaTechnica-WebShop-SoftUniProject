package bg.magna.websop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String viewIndex() {
        return "index";
    }

    @GetMapping("/contact")
    public String viewContact(Model model) {
        model.addAttribute("mapsApiKey", System.getenv("MAPS_API_KEY"));
        return "contact";
    }

}

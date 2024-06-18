package bg.magna.websop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String viewIndex() {
        return "index";
    }

    @GetMapping("/about")
    public String viewAbout() {
        return "about";
    }

}

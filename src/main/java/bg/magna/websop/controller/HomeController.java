package bg.magna.websop.controller;

import bg.magna.websop.model.dto.part.ShortPartDataDTO;
import bg.magna.websop.service.PartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String viewIndex() {
        return "index";
    }

    @GetMapping("/contact")
    public String viewContact() {
        return "contact";
    }

}

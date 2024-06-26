package bg.magna.websop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebShopController {

    @GetMapping("/webShop")
    public String viewWebShop() {
        return "webshop";
    }
}

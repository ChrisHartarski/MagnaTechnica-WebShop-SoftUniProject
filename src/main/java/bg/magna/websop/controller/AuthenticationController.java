package bg.magna.websop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/loginUser")
    public String viewLogin() {
        return "login";
    }
}

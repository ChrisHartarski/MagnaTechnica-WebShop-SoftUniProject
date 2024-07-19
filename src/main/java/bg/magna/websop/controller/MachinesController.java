package bg.magna.websop.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MachinesController {

    @GetMapping("/machines")
    public String machines(Model model) {
        return "machines";
    }
}

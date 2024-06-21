package bg.magna.websop.controller;

import bg.magna.websop.util.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelController {
    private UserSession userSession;

    public AdminPanelController(UserSession userSession) {
        this.userSession = userSession;
    }

    @GetMapping("/admin-panel")
    public String viewAdminPanel() {
        if(!userSession.isAdminLoggedIn()) {
            return "redirect:/";
        }
        return "admin-panel";
    }
}

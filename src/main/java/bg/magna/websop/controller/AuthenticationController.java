package bg.magna.websop.controller;

import bg.magna.websop.model.dto.LoginUserDTO;
import bg.magna.websop.model.dto.RegisterUserDTO;
import bg.magna.websop.service.UserService;
import bg.magna.websop.util.UserSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthenticationController {
    private final UserService userService;
    private final UserSession userSession;

    public AuthenticationController(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;
    }

    @ModelAttribute("registerData")
    public RegisterUserDTO registerUserDTO() {
        return new RegisterUserDTO();
    }

    @ModelAttribute("loginData")
    public LoginUserDTO loginUserDTO() {
        return new LoginUserDTO();
    }

    @GetMapping("/register")
    public String viewRegister() {
        if(userSession.isUserWithUserRoleLoggedIn()) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegisterUserDTO registerData,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if(userSession.isUserWithUserRoleLoggedIn()) {
            return "redirect:/";
        }

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerData", registerData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            return "redirect:/register";
        }

        if(!registerData.getPassword().equals(registerData.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("registerData", registerData);
            redirectAttributes.addFlashAttribute("passwordsDoNotMatch", true);
            return "redirect:/register";
        }

        if(userService.userEmailExists(registerData.getEmail())) {
            redirectAttributes.addFlashAttribute("registerData", registerData);
            redirectAttributes.addFlashAttribute("emailExists", true);
            return "redirect:/register";
        }

        userService.registerUser(registerData);
        return "redirect:/loginUser";
    }

    @GetMapping("/loginUser")
    public String viewLogin() {
        if(userSession.isUserLoggedIn()) {
            return "redirect:/";
        }
        return "loginUser";
    }

    @PostMapping("/loginUser")
    public String loginUser(@Valid LoginUserDTO loginData,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if(userSession.isUserLoggedIn()) {
            return "redirect:/";
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("loginData", loginData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginData", bindingResult);
            return "redirect:/loginUser";
        }

        if(!userService.isValidUser(loginData)) {
            redirectAttributes.addFlashAttribute("loginData", loginData);
            redirectAttributes.addFlashAttribute("wrongUsernameOrPassword", true);
            return "redirect:/loginUser";
        }

        userService.loginUser(loginData);
        return "redirect:/web-shop";
    }

    @PostMapping("/logout")
    public String logout(){
        if(!userSession.isUserLoggedIn()) {
            return "redirect:/";
        }

        userService.logoutUser();
        return "redirect:/";
    }
}

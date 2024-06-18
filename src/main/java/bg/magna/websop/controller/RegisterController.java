package bg.magna.websop.controller;

import bg.magna.websop.model.dto.RegisterUserDTO;
import bg.magna.websop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {
    private UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("registerData")
    public RegisterUserDTO registerUserDTO() {
        return new RegisterUserDTO();
    }

    @GetMapping("/register")
    public String viewRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegisterUserDTO registerData,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
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


}

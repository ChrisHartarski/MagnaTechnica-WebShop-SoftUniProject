package bg.magna.websop.controller;

import bg.magna.websop.model.MagnaUserDetails;
import bg.magna.websop.model.dto.*;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.service.CompanyService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.util.UserSession;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final CompanyService companyService;
    private final UserSession userSession;

    public UserController(UserService userService, CompanyService companyService, UserSession userSession) {
        this.userService = userService;
        this.companyService = companyService;
        this.userSession = userSession;
    }

    @ModelAttribute("userData")
    public UserDTO userDTO() {
        return new UserDTO();
    }

    @ModelAttribute("loginData")
    public ValidateUserDTO loginUserDTO() {
        return new ValidateUserDTO();
    }

    @GetMapping("/register")
    public String viewRegister(Model model) {

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid UserDTO userData,
                               @AuthenticationPrincipal MagnaUserDetails userDetails,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {


        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userData", bindingResult);
            return "redirect:/users/register";
        }

        if(!userData.getPassword().equals(userData.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("passwordsDoNotMatch", true);
            return "redirect:/users/register";
        }

        if(userService.userEmailExists(userData.getEmail())) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("emailExists", true);
            return "redirect:/users/register";
        }

        if(!companyService.companyExists(userData.getCompanyName())) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("companyDoesNotExist", true);
            return "redirect:/users/register";
        }

        userService.registerUser(userData);
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String viewLogin() {
        return "loginUser";
    }

    @GetMapping("/edit/{id}")
    public String viewEditUser(@PathVariable String id, @AuthenticationPrincipal MagnaUserDetails userDetails, Model model) {

        UserDTO userData = userService.getCurrentUserData(userDetails.getId());
        model.addAttribute("currentUserDetails", userDetails);
        model.addAttribute("userData", userData);

        return "edit-user";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable String id,
                           @Valid EditUserDTO userData,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {


        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userData", bindingResult);
            return "redirect:/users/edit/{id}";
        }

        if(!companyService.companyExists(userData.getCompanyName())) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("companyDoesNotExist", true);
            return "redirect:/users/edit/{id}";
        }

        userService.editUserData(userData, id);
        return "redirect:/";
    }

    @GetMapping("/edit/email/{id}")
    public String viewEditUserEmail(@PathVariable String id, Model model) {

        UserDTO userData = userService.getCurrentUserData(id);
        model.addAttribute("userData", userData);

        return "edit-user-email";
    }

    @PostMapping("/edit/email/{id}")
    public String editUserEmail(@PathVariable String id,
                                @Valid UserEmailDTO userData,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userData", bindingResult);
            return "redirect:/users/edit/email/{id}";
        }

        if(userService.userEmailExists(userData.getEmail())) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("emailExists", true);
            return "redirect:/users/edit/email/{id}";
        }

        userService.updateUserEmail(userData, id);
        return "redirect:/";
    }

    @GetMapping("/edit/password/{id}")
    public String viewEditUserPassoword(@PathVariable String id, Model model) {

        model.addAttribute("userPasswordData", new UserPasswordDTO());

        return "edit-user-password";
    }

    @PostMapping("/edit/password/{id}")
    public String editUserPassword(@PathVariable String id,
                                @Valid UserPasswordDTO userPasswordData,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userPasswordData", bindingResult);
            return "redirect:/users/edit/password/{id}";
        }

        if(!userService.isValidUser(new ValidateUserDTO(userSession.getEmail(), userPasswordData.getCurrentPassword()))) {
            redirectAttributes.addFlashAttribute("currentPasswordIncorrect", true);
            return "redirect:/users/edit/password/{id}";
        }

        if(!userPasswordData.getPassword().equals(userPasswordData.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("passwordsDoNotMatch", true);
            return "redirect:/users/edit/password/{id}";
        }

        userService.updateUserPassword(userPasswordData, id);
        return "redirect:/";
    }
}

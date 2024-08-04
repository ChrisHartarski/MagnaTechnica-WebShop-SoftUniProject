package bg.magna.websop.controller;

import bg.magna.websop.model.dto.user.*;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.service.CompanyService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.service.helper.UserHelperService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserHelperService userHelperService;

    public UserController(UserService userService, CompanyService companyService, UserHelperService userHelperService) {
        this.userService = userService;
        this.companyService = companyService;
        this.userHelperService = userHelperService;
    }

    @ModelAttribute("userData")
    public UserDTO userDTO() {
        return new UserDTO();
    }

    @ModelAttribute("loginData")
    public ValidateUserDTO loginUserDTO() {
        return new ValidateUserDTO();
    }

    @ModelAttribute("userPasswordData")
    public UserPasswordDTO userPasswordDTO() {
        UserPasswordDTO userPasswordData = new UserPasswordDTO();
        return userPasswordData;
    }

    @GetMapping("/register")
    public String viewRegister() {

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid UserDTO userData,
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

    @GetMapping("/login-error")
    public String viewLoginError(Model model) {

        model.addAttribute("wrongUsernameOrPassword", true);

        return "loginUser";
    }

    @GetMapping("/edit")
    public String viewEditUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        UserDTO userData = userService.getCurrentUserData(userDetails.getUsername());
        model.addAttribute("userData", userData);

        return "edit-user";
    }

    @PatchMapping("/edit")
    public String editUser(@AuthenticationPrincipal UserDetails userDetails,
                           @Valid EditUserDTO userData,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userData", bindingResult);
            return "redirect:/users/edit";
        }

        if(!companyService.companyExists(userData.getCompanyName())) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("companyDoesNotExist", true);
            return "redirect:/users/edit";
        }

        String userId = userService.getUserByEmail(userDetails.getUsername()).getId();
        userService.editUserData(userData, userId);
        userHelperService.updateAuthentication(userId);
        return "redirect:/";
    }

    @GetMapping("/edit/email")
    public String viewEditUserEmail(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        UserDTO userData = userService.getCurrentUserData(userDetails.getUsername());
        model.addAttribute("userData", userData);

        return "edit-user-email";
    }

    @PatchMapping("/edit/email")
    public String editUserEmail(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid UserEmailDTO userData,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userData", bindingResult);
            return "redirect:/users/edit/email";
        }

        if(userService.userEmailExists(userData.getEmail())) {
            redirectAttributes.addFlashAttribute("userData", userData);
            redirectAttributes.addFlashAttribute("emailExists", true);
            return "redirect:/users/edit/email";
        }

        String userId = userService.getUserByEmail(userDetails.getUsername()).getId();
        userService.editUserEmail(userData, userId);

        return "redirect:/";
    }

    @GetMapping("/edit/password")
    public String viewEditUserPassoword(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        UserEntity user = userService.getUserByEmail(userDetails.getUsername());
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO(user.getEmail());
        model.addAttribute("userPasswordData", userPasswordDTO);

        return "edit-user-password";
    }

    @PatchMapping("/edit/password")
    public String editUserPassword(@Valid UserPasswordDTO userPasswordData,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        UserEntity user = userService.getUserByEmail(userHelperService.getCurrentUserDetails().getUsername());

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userPasswordData", bindingResult);
            return "redirect:/users/edit/password";
        }

        if(!userService.isValidUser(new ValidateUserDTO(user.getEmail(), userPasswordData.getCurrentPassword()))) {
            redirectAttributes.addFlashAttribute("currentPasswordIncorrect", true);
            return "redirect:/users/edit/password";
        }

        if(!userPasswordData.getPassword().equals(userPasswordData.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("passwordsDoNotMatch", true);
            return "redirect:/users/edit/password";
        }

        userService.editUserPassword(userPasswordData, user.getId());

        return "redirect:/";
    }
}

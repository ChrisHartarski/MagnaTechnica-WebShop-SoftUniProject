package bg.magna.websop.controller;

import bg.magna.websop.model.dto.AddCompanyDTO;
import bg.magna.websop.model.dto.UserDTO;
import bg.magna.websop.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @ModelAttribute("companyData")
    public AddCompanyDTO companyData() {
        return new AddCompanyDTO();
    }

    @GetMapping("/add")
    public String viewAddCompany() {
        return "add-company";
    }

    @PostMapping("/add")
    public String addCompany(@Valid AddCompanyDTO companyData,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("companyData", companyData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.companyData", bindingResult);
            return "redirect:/company/add";
        }

        if(companyService.companyExists(companyData.getName())) {
            redirectAttributes.addFlashAttribute("companyData", companyData);
            redirectAttributes.addFlashAttribute("companyExists", true);
            return "redirect:/company/add";
        }

        if(companyService.companyWithVATExists(companyData.getVatNumber())) {
            redirectAttributes.addFlashAttribute("companyData", companyData);
            redirectAttributes.addFlashAttribute("VATnumberExists", true);
            return "redirect:/company/add";
        }

        companyService.addCompany(companyData);
        return "redirect:/users/register";
    }
}

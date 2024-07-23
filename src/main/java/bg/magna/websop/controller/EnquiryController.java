package bg.magna.websop.controller;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.dto.enquiry.AddEnquiryDTO;
import bg.magna.websop.model.dto.enquiry.FullEnquiryDTO;
import bg.magna.websop.service.EnquiryService;
import bg.magna.websop.service.helper.UserHelperService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/machines/enquiries")
public class EnquiryController {
    private final EnquiryService enquiryService;
    private final UserHelperService userHelperService;

    public EnquiryController(EnquiryService enquiryService, UserHelperService userHelperService) {
        this.enquiryService = enquiryService;
        this.userHelperService = userHelperService;
    }

    @GetMapping("/{id}")
    public String viewEnquiryScreen(@PathVariable("id") String id, Model model) {

        AddEnquiryDTO enquiryData = enquiryService.getAddEnquiryDTO(id);

        model.addAttribute("enquiryData", enquiryData);
        return "machine-enquiry";
    }

    @PostMapping("/{id}")
    public String addEnquiry(@PathVariable String id,
                             @Valid AddEnquiryDTO enquiryData,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("enquiryData", enquiryData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.enquiryData", bindingResult);
            redirectAttributes.addFlashAttribute("fieldsHaveErrors", true);
            return "redirect:/machines/enquiries/{id}";
        }

        CurrentUserDetails currentUser = userHelperService.getCurrentUserDetails();
        enquiryData.setMachineId(id);
        enquiryData.setUserId(currentUser.getId());
        enquiryService.addEnquiry(enquiryData);
        return "redirect:/machines";
    }

    @GetMapping("/all")
    public String viewAllEnquiries(Model model) {
        List<FullEnquiryDTO> enquiries = enquiryService.getAllEnquiries();
        model.addAttribute("enquiries", enquiries);
        return "enquiries";
    }

    @GetMapping("/view/{id}")
    public String viewEnquiry(@PathVariable long id, Model model) {
        FullEnquiryDTO enquiryData = enquiryService.getById(id);
        model.addAttribute("enquiry", enquiryData);
        return "enquiry-details";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEnquiry(@PathVariable long id) {
        enquiryService.delete(id);
        return "redirect:/machines/enquiries/all";
    }
}

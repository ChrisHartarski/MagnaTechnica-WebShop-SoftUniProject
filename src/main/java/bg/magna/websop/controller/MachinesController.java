package bg.magna.websop.controller;

import bg.magna.websop.model.dto.enquiry.AddEnquiryDTO;
import bg.magna.websop.model.dto.machine.AddMachineDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.dto.machine.ShortMachineDTO;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.MachineService;
import jakarta.validation.Valid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/machines")
public class MachinesController {
    private final MachineService machineService;
    private final BrandService brandService;

    public MachinesController(MachineService machineService, BrandService brandService) {
        this.machineService = machineService;
        this.brandService = brandService;
    }

    @ModelAttribute("machineData")
    public AddMachineDTO addMachineDTO() {
        return new AddMachineDTO();
    }

    @ModelAttribute("modifyMachineData")
    public FullMachineDTO modifyMachineDTO() {
        return new FullMachineDTO();
    }

    @ModelAttribute("enquiryData")
    public AddEnquiryDTO enquiryData() {
        return new AddEnquiryDTO();
    }

    @GetMapping()
    public String getMachines(Model model) {
        List<ShortMachineDTO> machines = machineService.getAll();
        model.addAttribute("machines", machines);

        return "machines";
    }

    @GetMapping("/{id}")
    public String getMachineDetails(@PathVariable("id") String id, Model model) {
        FullMachineDTO machine = machineService.getById(id);

        model.addAttribute("machine", machine);

        return "machine-details";
    }

    @DeleteMapping("/{id}")
    public String deleteMachine(@PathVariable("id") String id,
                                @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"))) {
            boolean result = machineService.deleteById(id);
        }
        return "redirect:/machines";
    }

    @GetMapping("/edit/{id}")
    public String viewUpdateMachine(@PathVariable("id") String id, Model model) {
        FullMachineDTO machineDTO = machineService.getById(id);

        model.addAttribute("modifyMachineData", machineDTO);
        model.addAttribute("allBrands", brandService.getAllBrandNames());

        return "edit-machine";
    }

    @PutMapping("/edit/{id}")
    public String updateMachine(@PathVariable("id") String id,
                                @Valid FullMachineDTO machineDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("modifyMachineData", machineDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.modifyMachineData", bindingResult);
            return "redirect:/machines/edit/{id}";
        }

        FullMachineDTO result = machineService.updateMachine(id, machineDTO);
        return "redirect:/machines/" + result.getId();
    }

    @GetMapping("/add")
    public String viewAddMachine(Model model) {

        model.addAttribute("allBrands", brandService.getAllBrandNames());

        return "add-machine";
    }

    @PostMapping("/add")
    public String addMachine(@Valid AddMachineDTO addMachineDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("machineData", addMachineDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.machineData", bindingResult);
            return "redirect:/machines/add";
        }

        if(machineService.machineExists(addMachineDTO.getSerialNumber())) {
            redirectAttributes.addFlashAttribute("machineData", addMachineDTO);
            redirectAttributes.addFlashAttribute("serialNumberExists", true);
            return "redirect:/machines/add";
        }

        machineService.addMachine(addMachineDTO);
        return "redirect:/machines";
    }
}

package bg.magna.websop.controller;

import bg.magna.websop.model.dto.AddMachineDTO;
import bg.magna.websop.model.dto.FullMachineDTO;
import bg.magna.websop.model.dto.PartDataDTO;
import bg.magna.websop.model.dto.ShortMachineDTO;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.MachineService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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

    @GetMapping("/")
    public String machines(Model model) {
        List<ShortMachineDTO> machines = machineService.getAll();
        model.addAttribute("machines", machines);

        return "machines";
    }

    @GetMapping("/{id}")
    public String getPartDetails(@PathVariable("id") String id, Model model) {
        FullMachineDTO machine = machineService.getById(id);

        model.addAttribute("machine", machine);

        return "machine-details";
    }

    @DeleteMapping("/{id}")
    public String deleteMachine(@PathVariable("id") String id,
                                @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails.getAuthorities().contains("ROLE_ADMIN")) {
            machineService.deleteById(id);
        }
        return "redirect:/machines";
    }

    @PutMapping("/edit/{id}")
    public String updateMachine(@PathVariable("id") String id,
                                @Valid FullMachineDTO machineDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal UserDetails userDetails) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("modifyMachineData", machineDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.modifyMachineData", bindingResult);
            return "redirect:/machines/edit/{id}";
        }

        machineService.updateMachine(machineDTO);
        return "redirect:/machines";
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

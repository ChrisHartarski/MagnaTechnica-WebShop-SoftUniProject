package bg.magna.websop.controller;

import bg.magna.websop.model.dto.AddMachineDTO;
import bg.magna.websop.model.dto.FullMachineDTO;
import bg.magna.websop.model.dto.PartDataDTO;
import bg.magna.websop.model.dto.ShortMachineDTO;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.MachineService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MachinesController {
    private final MachineService machineService;
    private final BrandService brandService;

    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_1 = new ShortMachineDTO("id1", "Akpil BisonXL 50", "https://www.akpil.pl/wp-content/uploads/2021/06/BISON-XL-3.png", 2022, "Akpil", "Trailed disc harrow Akpil BisonXL 50", "Прикачна дискова брана Akpil BisonXL 50");
    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_2 = new ShortMachineDTO("id2", "Akpil BisonXL 60", "https://www.akpil.pl/wp-content/uploads/2021/06/BISON-XL-3.png", 2023, "Akpil", "Trailed disc harrow Akpil BisonXL 60", "Прикачна дискова брана Akpil BisonXL 60");
    private static final FullMachineDTO TEST_FULL_MACHINE_DTO_2 = new FullMachineDTO("id2", "serialNum", "Akpil BisonXL 60", "https://www.akpil.pl/wp-content/uploads/2021/06/BISON-XL-3.png", 2023, "Akpil", "Trailed disc harrow Akpil BisonXL 60", "Прикачна дискова брана Akpil BisonXL 60", 6.00, 5750, 300, "moreInfoEn2", "moreInfoBg2");

    public MachinesController(MachineService machineService, BrandService brandService) {
        this.machineService = machineService;
        this.brandService = brandService;
    }

    @ModelAttribute("machineData")
    public AddMachineDTO addMachineDTO() {
        return new AddMachineDTO();
    }

    @GetMapping("/machines")
    public String machines(Model model) {
        List<ShortMachineDTO> machines = machineService.getAll();
//        List<ShortMachineDTO> machines = List.of(TEST_SHORT_MACHINE_DTO_1, TEST_SHORT_MACHINE_DTO_2);
        model.addAttribute("machines", machines);

        return "machines";
    }

    @GetMapping("/machines/{id}")
    public String getPartDetails(@PathVariable("id") String id, Model model) {

        FullMachineDTO machine = machineService.getById(id);
//        FullMachineDTO machine = TEST_FULL_MACHINE_DTO_2;

        model.addAttribute("machine", machine);

        return "machine-details";
    }

    @GetMapping("/machines/add")
    public String viewAddMachine(Model model) {

        model.addAttribute("allBrands", brandService.getAllBrandNames());

        return "add-machine";
    }

    @PostMapping("/machines/add")
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

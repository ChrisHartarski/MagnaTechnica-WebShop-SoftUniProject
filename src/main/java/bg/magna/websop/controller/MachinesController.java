package bg.magna.websop.controller;

import bg.magna.websop.model.dto.FullMachineDTO;
import bg.magna.websop.model.dto.PartDataDTO;
import bg.magna.websop.model.dto.ShortMachineDTO;
import bg.magna.websop.service.MachineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MachinesController {
    private final MachineService machineService;

    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_1 = new ShortMachineDTO("id1", "Akpil BisonXL 50", "https://www.akpil.pl/wp-content/uploads/2021/06/BISON-XL-3.png", 2022, "Akpil", "Trailed disc harrow Akpil BisonXL 50", "Прикачна дискова брана Akpil BisonXL 50");
    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_2 = new ShortMachineDTO("id2", "Akpil BisonXL 60", "https://www.akpil.pl/wp-content/uploads/2021/06/BISON-XL-3.png", 2023, "Akpil", "Trailed disc harrow Akpil BisonXL 60", "Прикачна дискова брана Akpil BisonXL 60");
    private static final FullMachineDTO TEST_FULL_MACHINE_DTO_2 = new FullMachineDTO("id2", "Akpil BisonXL 60", "https://www.akpil.pl/wp-content/uploads/2021/06/BISON-XL-3.png", 2023, "Akpil", "Trailed disc harrow Akpil BisonXL 60", "Прикачна дискова брана Akpil BisonXL 60", 6.00, 5750, 300, "moreInfo2");

    public MachinesController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping("/machines")
    public String machines(Model model) {
//        List<ShortMachineDTO> machines = machineService.getAll();
        List<ShortMachineDTO> machines = List.of(TEST_SHORT_MACHINE_DTO_1, TEST_SHORT_MACHINE_DTO_2);
        model.addAttribute("machines", machines);

        return "machines";
    }

    @GetMapping("/machines/{id}")
    public String getPartDetails(@PathVariable("id") String id, Model model) {

//        FullMachineDTO machine = machineService.getById(id);
        FullMachineDTO machine = TEST_FULL_MACHINE_DTO_2;

        model.addAttribute("machine", machine);

        return "machine-details";
    }
}

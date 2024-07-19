package bg.magna.websop.controller;

import bg.magna.websop.model.dto.ShortMachineDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MachinesController {
    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_1 = new ShortMachineDTO("id1", "Akpil BisonXL 50", "https://www.akpil.pl/wp-content/uploads/2021/06/BISON-XL-3.png", 2022, "Akpil", "Trailed disc harrow Akpil BisonXL 50", "Прикачна дискова брана Akpil BisonXL 50");
    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_2 = new ShortMachineDTO("id2", "Akpil BisonXL 60", "https://www.akpil.pl/wp-content/uploads/2021/06/BISON-XL-3.png", 2023, "Akpil", "Trailed disc harrow Akpil BisonXL 60", "Прикачна дискова брана Akpil BisonXL 60");

    @GetMapping("/machines")
    public String machines(Model model) {
        List<ShortMachineDTO> machines = List.of(TEST_SHORT_MACHINE_DTO_1, TEST_SHORT_MACHINE_DTO_2);
        model.addAttribute("machines", machines);

        return "machines";
    }
}

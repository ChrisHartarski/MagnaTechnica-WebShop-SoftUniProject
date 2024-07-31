package bg.magna.websop.service;

import bg.magna.websop.model.dto.machine.AddMachineDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.dto.machine.ShortMachineDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.io.IOException;
import java.util.List;

public interface MachineService {
    PagedModel<ShortMachineDTO> getAll(Pageable pageable);

    FullMachineDTO getById(String id);

    FullMachineDTO addMachine(AddMachineDTO addMachineDTO);

    boolean machineExists(String serialNumber);

    boolean deleteById(String id);

    FullMachineDTO updateMachine(String id, FullMachineDTO machineDTO);

    boolean initializeMockMachines() throws IOException;
}

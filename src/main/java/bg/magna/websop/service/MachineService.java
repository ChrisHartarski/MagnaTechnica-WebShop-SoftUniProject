package bg.magna.websop.service;

import bg.magna.websop.model.dto.machine.AddMachineDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.dto.machine.ShortMachineDTO;
import bg.magna.websop.util.CustomPagedModel;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface MachineService {

    CustomPagedModel<ShortMachineDTO> getAll(Pageable pageable);

    FullMachineDTO getById(String id);

    FullMachineDTO addMachine(AddMachineDTO addMachineDTO);

    boolean machineExists(String serialNumber);

    boolean deleteById(String id);

    FullMachineDTO updateMachine(String id, FullMachineDTO machineDTO);

    boolean initializeMockMachines() throws IOException;
}

package bg.magna.websop.service;

import bg.magna.websop.model.dto.machine.AddMachineDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.dto.machine.ShortMachineDTO;

import java.io.IOException;
import java.util.List;

public interface MachineService {
    List<ShortMachineDTO> getAll();

    FullMachineDTO getById(String id);

    void addMachine(AddMachineDTO addMachineDTO);

    boolean machineExists(String serialNumber);

    void deleteById(String id);

    void updateMachine(FullMachineDTO machineDTO);

    void initializeMockMachines() throws IOException;
}

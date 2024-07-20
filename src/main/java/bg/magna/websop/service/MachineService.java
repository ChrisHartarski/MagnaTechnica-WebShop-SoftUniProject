package bg.magna.websop.service;

import bg.magna.websop.model.dto.AddMachineDTO;
import bg.magna.websop.model.dto.FullMachineDTO;
import bg.magna.websop.model.dto.ShortMachineDTO;

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

package bg.magna.websop.service;

import bg.magna.websop.model.dto.FullMachineDTO;
import bg.magna.websop.model.dto.ShortMachineDTO;

import java.util.List;

public interface MachineService {
    List<ShortMachineDTO> getAll();

    FullMachineDTO getById(String id);
}

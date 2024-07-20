package bg.magna.websop.service.impl;

import bg.magna.websop.config.MachinesApiConfig;
import bg.magna.websop.model.dto.AddMachineDTO;
import bg.magna.websop.model.dto.FullMachineDTO;
import bg.magna.websop.model.dto.ShortMachineDTO;
import bg.magna.websop.service.MachineService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {
    private final RestClient machineRestClient;

    public MachineServiceImpl(RestClient machineRestClient) {
        this.machineRestClient = machineRestClient;
    }

    @Override
    public List<ShortMachineDTO> getAll() {
        return machineRestClient
                .get()
                .uri("/machines/all")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }

    @Override
    public FullMachineDTO getById(String id) {
        return machineRestClient
                .get()
                .uri("/machines/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(FullMachineDTO.class);
    }

    @Override
    public void addMachine(AddMachineDTO addMachineDTO) {
        machineRestClient
                .post()
                .uri("/machines/add")
                .contentType(MediaType.APPLICATION_JSON)
                        .body(addMachineDTO)
                                .retrieve();
    }

    @Override
    public boolean machineExists(String serialNumber) {
        Boolean machineExists = machineRestClient
                .get()
                .uri("/machines/exist/" + serialNumber)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Boolean.class);

        if(machineExists == null) {
            machineExists = false;
        }

        return machineExists;
    }
}

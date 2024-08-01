package bg.magna.websop.service.impl;

import bg.magna.websop.config.MachinesApiConfig;
import bg.magna.websop.model.dto.machine.AddMachineDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.dto.machine.ShortMachineDTO;
import bg.magna.websop.service.MachineService;
import bg.magna.websop.service.exception.ResourceNotFoundException;
import bg.magna.websop.util.CustomPagedModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class MachineServiceImpl implements MachineService {
    private final RestClient machineRestClient;
    private final MachinesApiConfig machinesApiConfig;
    private final Gson gson;
    private final ObjectMapper objectMapper;
    private static final String MACHINES_JSON_FILE_PATH = "src/main/resources/data/machines.json";

    public MachineServiceImpl(RestClient machineRestClient, MachinesApiConfig machinesApiConfig, Gson gson, ObjectMapper objectMapper) {
        this.machineRestClient = machineRestClient;
        this.machinesApiConfig = machinesApiConfig;
        this.gson = gson;
        this.objectMapper = objectMapper;
    }

    @Override
    public CustomPagedModel<ShortMachineDTO> getAll(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();

        return machineRestClient.get()
                .uri(machinesApiConfig.getBaseUrl() + "/machines/all" + "?page=" + pageNumber)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<CustomPagedModel<ShortMachineDTO>>() {});
    }

    @Override
    public FullMachineDTO getById(String id) {
        return machineRestClient
                .get()
                .uri(machinesApiConfig.getBaseUrl() + "/machines/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(FullMachineDTO.class);
    }

    @Override
    public FullMachineDTO addMachine(AddMachineDTO addMachineDTO) {
        return machineRestClient
                .post()
                .uri(machinesApiConfig.getBaseUrl() + "/machines/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(addMachineDTO)
                .retrieve()
                .body(FullMachineDTO.class);
    }

    @Override
    public boolean machineExists(String serialNumber) {
        Boolean machineExists = machineRestClient
                .get()
                .uri(machinesApiConfig.getBaseUrl() + "/machines/exist/" + serialNumber)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Boolean.class);

        if(machineExists == null) {
            machineExists = false;
        }

        return machineExists;
    }

    @Override
    public boolean deleteById(String id) {
        if(machineExists(getById(id).getSerialNumber())) {
            Boolean result = machineRestClient
                    .delete()
                    .uri(machinesApiConfig.getBaseUrl() + "/machines/" + id)
                    .retrieve()
                    .body(Boolean.class);

            if(result == null) {
                return false;
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public FullMachineDTO updateMachine(String id, FullMachineDTO machineDTO) {
        if(machineExists(machineDTO.getSerialNumber())) {
            return machineRestClient
                    .put()
                    .uri(machinesApiConfig.getBaseUrl() + "/machines/edit/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(machineDTO)
                    .retrieve()
                    .body(FullMachineDTO.class);
        } else {
            throw new ResourceNotFoundException("Machine was not found");
        }
    }

    @Override
    public boolean initializeMockMachines() throws IOException {
        if(machineRepositoryEmpty()) {
            Arrays.stream(gson.fromJson(Files.readString(Path.of(MACHINES_JSON_FILE_PATH)), AddMachineDTO[].class))
                    .forEach(this::addMachine);
            return true;
        } else {
            return false;
        }
    }

    private boolean machineRepositoryEmpty() {
        Boolean machineRepositoryEmpty = machineRestClient
                .get()
                .uri(machinesApiConfig.getBaseUrl() + "/machines/repository/empty")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Boolean.class);

        if(machineRepositoryEmpty == null) {
            machineRepositoryEmpty = false;
        }

        return machineRepositoryEmpty;
    }
}

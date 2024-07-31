package bg.magna.websop.controller;

import bg.magna.websop.model.dto.machine.AddMachineDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.dto.machine.ShortMachineDTO;
import bg.magna.websop.repository.BrandRepository;
import bg.magna.websop.service.BrandService;
import bg.magna.websop.service.MachineService;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MachinesControllerIT {
    private static final FullMachineDTO TEST_FULL_MACHINE_DTO_1 = new FullMachineDTO("UUID1", "serial1", "machine1", "https://image1URL.com", 2021, "Akpil", "descriptionEn1", "descriptionBg1", 1.0, 1, 111, "moreInfoEn1", "moreInfoBg1");
    private static final FullMachineDTO TEST_FULL_MACHINE_DTO_2 = new FullMachineDTO("UUID2", "serial2", "machine2", "https://image2URL.com", 2022, "Arbos", "descriptionEn2", "descriptionBg2", 2.0, 2, 222, "moreInfoEn2", "moreInfoBg2");
    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_1 = new ShortMachineDTO(TEST_FULL_MACHINE_DTO_1.getId(), TEST_FULL_MACHINE_DTO_1.getName(), TEST_FULL_MACHINE_DTO_1.getImageURL(), TEST_FULL_MACHINE_DTO_1.getYear(), TEST_FULL_MACHINE_DTO_1.getBrandName(), TEST_FULL_MACHINE_DTO_1.getDescriptionEn(), TEST_FULL_MACHINE_DTO_1.getDescriptionBg(), LocalDateTime.now());
    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_2 = new ShortMachineDTO(TEST_FULL_MACHINE_DTO_2.getId(), TEST_FULL_MACHINE_DTO_2.getName(), TEST_FULL_MACHINE_DTO_2.getImageURL(), TEST_FULL_MACHINE_DTO_2.getYear(), TEST_FULL_MACHINE_DTO_2.getBrandName(), TEST_FULL_MACHINE_DTO_2.getDescriptionEn(), TEST_FULL_MACHINE_DTO_2.getDescriptionBg(), LocalDateTime.now());
    private static final AddMachineDTO TEST_ADD_MACHINE_DTO_1 = new AddMachineDTO(TEST_FULL_MACHINE_DTO_1.getSerialNumber(), TEST_FULL_MACHINE_DTO_1.getName(), TEST_FULL_MACHINE_DTO_1.getImageURL(), TEST_FULL_MACHINE_DTO_1.getYear(), TEST_FULL_MACHINE_DTO_1.getBrandName(), TEST_FULL_MACHINE_DTO_1.getDescriptionEn(), TEST_FULL_MACHINE_DTO_1.getDescriptionBg(), TEST_FULL_MACHINE_DTO_1.getWorkingWidth(), TEST_FULL_MACHINE_DTO_1.getWeight(), TEST_FULL_MACHINE_DTO_1.getRequiredPower(), TEST_FULL_MACHINE_DTO_1.getMoreInfoEn(), TEST_FULL_MACHINE_DTO_1.getMoreInfoBg());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandService brandService;

    @MockBean
    private MachineService machineService;

    @BeforeEach
    public void setUp() {
        brandService.initializeMockBrands();
    }

    @AfterEach
    public void tearDown() {
        brandRepository.deleteAll();
    }

//    @Test
//    public void testGetMachines() throws Exception {
//
//        when(machineService.getAll(Pageable.ofSize(2))).thenReturn(new PagedModel<>(new PageImpl<>(List.of(TEST_SHORT_MACHINE_DTO_1, TEST_SHORT_MACHINE_DTO_2))));
//
//        mockMvc.perform(get("/machines/all"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("machines"))
//                .andExpect(view().name("machines"));
//    }

    @Test
    public void testGetMachineDetails() throws Exception {
        when(machineService.getById(TEST_FULL_MACHINE_DTO_1.getId())).thenReturn(TEST_FULL_MACHINE_DTO_1);

        mockMvc.perform(get("/machines/" + TEST_FULL_MACHINE_DTO_1.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("machine", TEST_FULL_MACHINE_DTO_1))
                .andExpect(view().name("machine-details"));
    }

    @Test
    public void testDeleteMachine() throws Exception {
        when(machineService.deleteById(TEST_FULL_MACHINE_DTO_1.getId())).thenReturn(true);

        mockMvc.perform(delete("/machines/" + TEST_FULL_MACHINE_DTO_1.getId())
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/machines"));
    }

    @Test
    public void testViewUpdateMachine() throws Exception {
        when(machineService.getById(TEST_FULL_MACHINE_DTO_1.getId())).thenReturn(TEST_FULL_MACHINE_DTO_1);

        mockMvc.perform(get("/machines/edit/" + TEST_FULL_MACHINE_DTO_1.getId())
                .with(user("admin@example.com").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("modifyMachineData", TEST_FULL_MACHINE_DTO_1))
                .andExpect(view().name("edit-machine"));
    }

    @Test
    public void testUpdateMachine() throws Exception {
        FullMachineDTO expected = new FullMachineDTO();
        expected.setId(TEST_FULL_MACHINE_DTO_1.getId());
        expected.setSerialNumber(TEST_FULL_MACHINE_DTO_1.getSerialNumber());
        expected.setName(TEST_FULL_MACHINE_DTO_1.getName());
        expected.setBrandName(TEST_FULL_MACHINE_DTO_1.getBrandName());
        expected.setYear(TEST_FULL_MACHINE_DTO_1.getYear());
        expected.setImageURL(TEST_FULL_MACHINE_DTO_2.getImageURL());
        expected.setDescriptionEn(TEST_FULL_MACHINE_DTO_2.getDescriptionEn());
        expected.setDescriptionBg(TEST_FULL_MACHINE_DTO_2.getDescriptionBg());
        expected.setWorkingWidth(TEST_FULL_MACHINE_DTO_2.getWorkingWidth());
        expected.setWeight(TEST_FULL_MACHINE_DTO_2.getWeight());
        expected.setRequiredPower(TEST_FULL_MACHINE_DTO_2.getRequiredPower());
        expected.setMoreInfoEn(TEST_FULL_MACHINE_DTO_2.getMoreInfoEn());
        expected.setMoreInfoBg(TEST_FULL_MACHINE_DTO_2.getMoreInfoBg());

        when(machineService.updateMachine(eq(expected.getId()), any())).thenReturn(expected);
        when(machineService.getById(expected.getId())).thenReturn(expected);

        mockMvc.perform(put("/machines/edit/" + expected.getId())
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf())
                        .param("id", expected.getId())
                        .param("serialNumber", expected.getSerialNumber())
                        .param("name", expected.getName())
                        .param("imageURL", expected.getImageURL())
                        .param("year", String.valueOf(expected.getYear()))
                        .param("brandName", expected.getBrandName())
                        .param("descriptionEn", expected.getDescriptionEn())
                        .param("descriptionBg", expected.getDescriptionBg())
                        .param("workingWidth", String.valueOf(expected.getWorkingWidth()))
                        .param("weight", String.valueOf(expected.getWeight()))
                        .param("requiredPower", String.valueOf(expected.getRequiredPower()))
                        .param("moreInfoEn", expected.getMoreInfoEn())
                        .param("moreInfoBg", expected.getMoreInfoBg()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/machines/" + expected.getId()));
    }

    @Test
    public void testViewAddMachine() throws Exception {

        mockMvc.perform(get("/machines/add")
                .with(user("admin@example.com").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allBrands"))
                .andExpect(view().name("add-machine"));
    }

    @Test
    public void testAddMachine() throws Exception {
        when(machineService.addMachine(TEST_ADD_MACHINE_DTO_1)).thenReturn(TEST_FULL_MACHINE_DTO_1);

        mockMvc.perform(post("/machines/add")
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf())
                        .param("serialNumber", TEST_ADD_MACHINE_DTO_1.getSerialNumber())
                        .param("name", TEST_ADD_MACHINE_DTO_1.getName())
                        .param("imageURL", TEST_ADD_MACHINE_DTO_1.getImageURL())
                        .param("year", String.valueOf(TEST_ADD_MACHINE_DTO_1.getYear()))
                        .param("brandName", TEST_ADD_MACHINE_DTO_1.getBrandName())
                        .param("descriptionEn", TEST_ADD_MACHINE_DTO_1.getDescriptionEn())
                        .param("descriptionBg", TEST_ADD_MACHINE_DTO_1.getDescriptionBg())
                        .param("workingWidth", String.valueOf(TEST_ADD_MACHINE_DTO_1.getWorkingWidth()))
                        .param("weight", String.valueOf(TEST_ADD_MACHINE_DTO_1.getWeight()))
                        .param("requiredPower", String.valueOf(TEST_ADD_MACHINE_DTO_1.getRequiredPower()))
                        .param("moreInfoEn", TEST_ADD_MACHINE_DTO_1.getMoreInfoEn())
                        .param("moreInfoBg", TEST_ADD_MACHINE_DTO_1.getMoreInfoBg()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/machines"));
    }

    @Test
    public void testAddMachine_returnsIfInputInvalid() throws Exception {
        when(machineService.addMachine(TEST_ADD_MACHINE_DTO_1)).thenReturn(TEST_FULL_MACHINE_DTO_1);

        mockMvc.perform(post("/machines/add")
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf())
                        .param("serialNumber", TEST_ADD_MACHINE_DTO_1.getSerialNumber())
                        .param("name", "")
                        .param("imageURL", "")
                        .param("year", String.valueOf(TEST_ADD_MACHINE_DTO_1.getYear()))
                        .param("brandName", "")
                        .param("descriptionEn", "")
                        .param("descriptionBg", "")
                        .param("workingWidth", String.valueOf(TEST_ADD_MACHINE_DTO_1.getWorkingWidth()))
                        .param("weight", String.valueOf(TEST_ADD_MACHINE_DTO_1.getWeight()))
                        .param("requiredPower", String.valueOf(TEST_ADD_MACHINE_DTO_1.getRequiredPower()))
                        .param("moreInfoEn", TEST_ADD_MACHINE_DTO_1.getMoreInfoEn())
                        .param("moreInfoBg", TEST_ADD_MACHINE_DTO_1.getMoreInfoBg()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/machines/add"));
    }

    @Test
    public void testAddMachine_returnsIfMachineExists() throws Exception {
        when(machineService.addMachine(TEST_ADD_MACHINE_DTO_1)).thenReturn(TEST_FULL_MACHINE_DTO_1);
        when(machineService.machineExists(TEST_ADD_MACHINE_DTO_1.getSerialNumber())).thenReturn(true);

        mockMvc.perform(post("/machines/add")
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf())
                        .param("serialNumber", TEST_ADD_MACHINE_DTO_1.getSerialNumber())
                        .param("name", TEST_ADD_MACHINE_DTO_1.getName())
                        .param("imageURL", TEST_ADD_MACHINE_DTO_1.getImageURL())
                        .param("year", String.valueOf(TEST_ADD_MACHINE_DTO_1.getYear()))
                        .param("brandName", TEST_ADD_MACHINE_DTO_1.getBrandName())
                        .param("descriptionEn", TEST_ADD_MACHINE_DTO_1.getDescriptionEn())
                        .param("descriptionBg", TEST_ADD_MACHINE_DTO_1.getDescriptionBg())
                        .param("workingWidth", String.valueOf(TEST_ADD_MACHINE_DTO_1.getWorkingWidth()))
                        .param("weight", String.valueOf(TEST_ADD_MACHINE_DTO_1.getWeight()))
                        .param("requiredPower", String.valueOf(TEST_ADD_MACHINE_DTO_1.getRequiredPower()))
                        .param("moreInfoEn", TEST_ADD_MACHINE_DTO_1.getMoreInfoEn())
                        .param("moreInfoBg", TEST_ADD_MACHINE_DTO_1.getMoreInfoBg()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/machines/add"))
                .andExpect(flash().attribute("serialNumberExists", true));
    }
}

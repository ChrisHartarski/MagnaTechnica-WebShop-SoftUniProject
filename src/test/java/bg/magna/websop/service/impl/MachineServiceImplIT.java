package bg.magna.websop.service.impl;

import bg.magna.websop.config.MachinesApiConfig;
import bg.magna.websop.model.dto.machine.AddMachineDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.dto.machine.ShortMachineDTO;
import bg.magna.websop.service.MachineService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(name = "machines-api-service"))
public class MachineServiceImplIT {
    private static final Gson gson = new Gson();
    private static final FullMachineDTO TEST_FULL_MACHINE_DTO_1 = new FullMachineDTO("UUID1", "serial1", "machine1", "image1URL", 2021, "brandName1", "descriptionEn1", "descriptionBg1", 1.0, 1, 111, "moreInfoEn1", "moreInfoBg1");
    private static final FullMachineDTO TEST_FULL_MACHINE_DTO_2 = new FullMachineDTO("UUID2", "serial2", "machine2", "image2URL", 2022, "brandName2", "descriptionEn2", "descriptionBg2", 2.0, 2, 222, "moreInfoEn2", "moreInfoBg2");
    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_1 = new ShortMachineDTO(TEST_FULL_MACHINE_DTO_1.getId(), TEST_FULL_MACHINE_DTO_1.getName(), TEST_FULL_MACHINE_DTO_1.getImageURL(), TEST_FULL_MACHINE_DTO_1.getYear(), TEST_FULL_MACHINE_DTO_1.getBrandName(), TEST_FULL_MACHINE_DTO_1.getDescriptionEn(), TEST_FULL_MACHINE_DTO_1.getDescriptionBg());
    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_2 = new ShortMachineDTO(TEST_FULL_MACHINE_DTO_2.getId(), TEST_FULL_MACHINE_DTO_2.getName(), TEST_FULL_MACHINE_DTO_2.getImageURL(), TEST_FULL_MACHINE_DTO_2.getYear(), TEST_FULL_MACHINE_DTO_2.getBrandName(), TEST_FULL_MACHINE_DTO_2.getDescriptionEn(), TEST_FULL_MACHINE_DTO_2.getDescriptionBg());
    private static final AddMachineDTO TEST_ADD_MACHINE_DTO_1 = new AddMachineDTO(TEST_FULL_MACHINE_DTO_1.getSerialNumber(), TEST_FULL_MACHINE_DTO_1.getName(), TEST_FULL_MACHINE_DTO_1.getImageURL(), TEST_FULL_MACHINE_DTO_1.getYear(), TEST_FULL_MACHINE_DTO_1.getBrandName(), TEST_FULL_MACHINE_DTO_1.getDescriptionEn(), TEST_FULL_MACHINE_DTO_1.getDescriptionBg(), TEST_FULL_MACHINE_DTO_1.getWorkingWidth(), TEST_FULL_MACHINE_DTO_1.getWeight(), TEST_FULL_MACHINE_DTO_1.getRequiredPower(), TEST_FULL_MACHINE_DTO_1.getMoreInfoEn(), TEST_FULL_MACHINE_DTO_1.getMoreInfoBg());
    private static final String JSON_LIST_SHORT_MACHINE_DTO = gson.toJson(List.of(TEST_SHORT_MACHINE_DTO_1, TEST_SHORT_MACHINE_DTO_2));
    private static final String JSON_FULL_MACHINE_DTO_1 = gson.toJson(TEST_FULL_MACHINE_DTO_1);
    private static final String JSON_ADD_MACHINE_DTO_1 = gson.toJson(TEST_ADD_MACHINE_DTO_1);

    @InjectWireMock(value = "machines-api-service")
    private WireMockServer wireMockServer;

    @Autowired
    private MachineService machineService;

    @Autowired
    private MachinesApiConfig machinesApiConfig;

    @BeforeEach
    void setUp() {
        machinesApiConfig.setBaseUrl(wireMockServer.baseUrl() + "/test-machines");
    }

    @Test
    public void testGetAll() {
        wireMockServer.stubFor(get("/test-machines/machines/all")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(JSON_LIST_SHORT_MACHINE_DTO)
                )
        );

        List<ShortMachineDTO> expected = List.of(TEST_SHORT_MACHINE_DTO_1, TEST_SHORT_MACHINE_DTO_2);
        List<ShortMachineDTO> actual = machineService.getAll();

        Assertions.assertEquals(expected.size(), actual.size());

        Assertions.assertEquals(expected.get(0).getId(), actual.get(0).getId());
        Assertions.assertEquals(expected.get(0).getName(), actual.get(0).getName());
        Assertions.assertEquals(expected.get(0).getBrandName(), actual.get(0).getBrandName());
        Assertions.assertEquals(expected.get(0).getYear(), actual.get(0).getYear());
        Assertions.assertEquals(expected.get(0).getImageURL(), actual.get(0).getImageURL());
        Assertions.assertEquals(expected.get(0).getDescriptionEn(), actual.get(0).getDescriptionEn());
        Assertions.assertEquals(expected.get(0).getDescriptionBg(), actual.get(0).getDescriptionBg());

        Assertions.assertEquals(expected.get(1).getId(), actual.get(1).getId());
        Assertions.assertEquals(expected.get(1).getName(), actual.get(1).getName());
        Assertions.assertEquals(expected.get(1).getBrandName(), actual.get(1).getBrandName());
        Assertions.assertEquals(expected.get(1).getYear(), actual.get(1).getYear());
        Assertions.assertEquals(expected.get(1).getImageURL(), actual.get(1).getImageURL());
        Assertions.assertEquals(expected.get(1).getDescriptionEn(), actual.get(1).getDescriptionEn());
        Assertions.assertEquals(expected.get(1).getDescriptionBg(), actual.get(1).getDescriptionBg());
    }

    @Test
    public void testGetById() {
        wireMockServer.stubFor(get("/test-machines/machines/" + TEST_FULL_MACHINE_DTO_1.getId())
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(JSON_FULL_MACHINE_DTO_1)
                )
        );

        FullMachineDTO expected = TEST_FULL_MACHINE_DTO_1;
        FullMachineDTO actual = machineService.getById(TEST_FULL_MACHINE_DTO_1.getId());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getBrandName(), actual.getBrandName());
        Assertions.assertEquals(expected.getYear(), actual.getYear());
        Assertions.assertEquals(expected.getImageURL(), actual.getImageURL());
        Assertions.assertEquals(expected.getDescriptionEn(), actual.getDescriptionEn());
        Assertions.assertEquals(expected.getDescriptionBg(), actual.getDescriptionBg());
        Assertions.assertEquals(expected.getWorkingWidth(), actual.getWorkingWidth());
        Assertions.assertEquals(expected.getWeight(), actual.getWeight());
        Assertions.assertEquals(expected.getRequiredPower(), actual.getRequiredPower());
        Assertions.assertEquals(expected.getMoreInfoEn(), actual.getMoreInfoEn());
        Assertions.assertEquals(expected.getMoreInfoBg(), actual.getMoreInfoBg());
    }

//    @Test
//    public void testAddMachine() {
//        wireMockServer.stubFor(get("/test-machines/machines/add")
//                        .withRequestBody();
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", "application/json")
//                        .withStatus(201)
//                        .withBody(JSON_FULL_MACHINE_DTO_1)
//                )
//        );
//
//        FullMachineDTO expected = TEST_FULL_MACHINE_DTO_1;
//        FullMachineDTO actual = machineService.addMachine(TEST_ADD_MACHINE_DTO_1);
//
//        Assertions.assertNotNull(actual);
//        Assertions.assertEquals(expected.getId(), actual.getId());
//        Assertions.assertEquals(expected.getName(), actual.getName());
//        Assertions.assertEquals(expected.getBrandName(), actual.getBrandName());
//        Assertions.assertEquals(expected.getYear(), actual.getYear());
//        Assertions.assertEquals(expected.getImageURL(), actual.getImageURL());
//        Assertions.assertEquals(expected.getDescriptionEn(), actual.getDescriptionEn());
//        Assertions.assertEquals(expected.getDescriptionBg(), actual.getDescriptionBg());
//        Assertions.assertEquals(expected.getWorkingWidth(), actual.getWorkingWidth());
//        Assertions.assertEquals(expected.getWeight(), actual.getWeight());
//        Assertions.assertEquals(expected.getRequiredPower(), actual.getRequiredPower());
//        Assertions.assertEquals(expected.getMoreInfoEn(), actual.getMoreInfoEn());
//        Assertions.assertEquals(expected.getMoreInfoBg(), actual.getMoreInfoBg());
//    }
}


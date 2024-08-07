package bg.magna.websop.service.impl;

import bg.magna.websop.config.MachinesApiConfig;
import bg.magna.websop.model.dto.machine.AddMachineDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.dto.machine.ShortMachineDTO;
import bg.magna.websop.service.MachineService;
import bg.magna.websop.util.CustomPagedModel;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(name = "machines-api-service"))
public class MachineServiceImplIT {

    private static final FullMachineDTO TEST_FULL_MACHINE_DTO_1 = new FullMachineDTO("UUID1", "serial1", "machine1", "image1URL", 2021, "brandName1", "descriptionEn1", "descriptionBg1", 1.0, 1, 111, "moreInfoEn1", "moreInfoBg1");
    private static final FullMachineDTO TEST_FULL_MACHINE_DTO_2 = new FullMachineDTO("UUID2", "serial2", "machine2", "image2URL", 2022, "brandName2", "descriptionEn2", "descriptionBg2", 2.0, 2, 222, "moreInfoEn2", "moreInfoBg2");
    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_1 = new ShortMachineDTO(TEST_FULL_MACHINE_DTO_1.getId(), TEST_FULL_MACHINE_DTO_1.getName(), TEST_FULL_MACHINE_DTO_1.getImageURL(), TEST_FULL_MACHINE_DTO_1.getYear(), TEST_FULL_MACHINE_DTO_1.getBrandName(), TEST_FULL_MACHINE_DTO_1.getDescriptionEn(), TEST_FULL_MACHINE_DTO_1.getDescriptionBg(), LocalDateTime.now());
    private static final ShortMachineDTO TEST_SHORT_MACHINE_DTO_2 = new ShortMachineDTO(TEST_FULL_MACHINE_DTO_2.getId(), TEST_FULL_MACHINE_DTO_2.getName(), TEST_FULL_MACHINE_DTO_2.getImageURL(), TEST_FULL_MACHINE_DTO_2.getYear(), TEST_FULL_MACHINE_DTO_2.getBrandName(), TEST_FULL_MACHINE_DTO_2.getDescriptionEn(), TEST_FULL_MACHINE_DTO_2.getDescriptionBg(), LocalDateTime.now());
    private static final AddMachineDTO TEST_ADD_MACHINE_DTO_1 = new AddMachineDTO(TEST_FULL_MACHINE_DTO_1.getSerialNumber(), TEST_FULL_MACHINE_DTO_1.getName(), TEST_FULL_MACHINE_DTO_1.getImageURL(), TEST_FULL_MACHINE_DTO_1.getYear(), TEST_FULL_MACHINE_DTO_1.getBrandName(), TEST_FULL_MACHINE_DTO_1.getDescriptionEn(), TEST_FULL_MACHINE_DTO_1.getDescriptionBg(), TEST_FULL_MACHINE_DTO_1.getWorkingWidth(), TEST_FULL_MACHINE_DTO_1.getWeight(), TEST_FULL_MACHINE_DTO_1.getRequiredPower(), TEST_FULL_MACHINE_DTO_1.getMoreInfoEn(), TEST_FULL_MACHINE_DTO_1.getMoreInfoBg());

    @InjectWireMock(value = "machines-api-service")
    private WireMockServer wireMockServer;

    @Autowired
    private MachineService machineService;

    @Autowired
    private MachinesApiConfig machinesApiConfig;

    @Autowired
    private Gson gson;

    @BeforeEach
    void setUp() {
        machinesApiConfig.setBaseUrl(wireMockServer.baseUrl() + "/test-machines");
        wireMockServer.resetAll();
    }

    @Test
    public void testGetAll() {
        wireMockServer.stubFor(get("/test-machines/machines/all?page=0")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(getJsonCustomPagedModel())
                )
        );

        CustomPagedModel<ShortMachineDTO> expected = new CustomPagedModel<>(List.of(TEST_SHORT_MACHINE_DTO_1, TEST_SHORT_MACHINE_DTO_2),
                                                                            new PagedModel.PageMetadata(2, 0, 2, 1));

        CustomPagedModel<ShortMachineDTO> actual = machineService.getAll(Pageable.ofSize(2));

        Assertions.assertEquals(expected.getPage().size(), actual.getPage().size());

        Assertions.assertEquals(expected.getContent().getFirst().getId(), actual.getContent().getFirst().getId());
        Assertions.assertEquals(expected.getContent().getFirst().getName(), actual.getContent().getFirst().getName());
        Assertions.assertEquals(expected.getContent().getFirst().getBrandName(), actual.getContent().getFirst().getBrandName());
        Assertions.assertEquals(expected.getContent().getFirst().getYear(), actual.getContent().getFirst().getYear());
        Assertions.assertEquals(expected.getContent().getFirst().getImageURL(), actual.getContent().getFirst().getImageURL());
        Assertions.assertEquals(expected.getContent().getFirst().getDescriptionEn(), actual.getContent().getFirst().getDescriptionEn());
        Assertions.assertEquals(expected.getContent().getFirst().getDescriptionBg(), actual.getContent().getFirst().getDescriptionBg());

        Assertions.assertEquals(expected.getContent().get(1).getId(), actual.getContent().get(1).getId());
        Assertions.assertEquals(expected.getContent().get(1).getName(), actual.getContent().get(1).getName());
        Assertions.assertEquals(expected.getContent().get(1).getBrandName(), actual.getContent().get(1).getBrandName());
        Assertions.assertEquals(expected.getContent().get(1).getYear(), actual.getContent().get(1).getYear());
        Assertions.assertEquals(expected.getContent().get(1).getImageURL(), actual.getContent().get(1).getImageURL());
        Assertions.assertEquals(expected.getContent().get(1).getDescriptionEn(), actual.getContent().get(1).getDescriptionEn());
        Assertions.assertEquals(expected.getContent().get(1).getDescriptionBg(), actual.getContent().get(1).getDescriptionBg());
    }

    @Test
    public void testGetById() {
        wireMockServer.stubFor(get("/test-machines/machines/" + TEST_FULL_MACHINE_DTO_1.getId())
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(getJsonFullMachineDto())
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

    @Test
    public void testAddMachine() {
        wireMockServer.stubFor(post(urlEqualTo("/test-machines/machines/add"))
                        .withHeader("Content-Type", equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .withRequestBody(equalToJson(getJsonAddMachineDto()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(201)
                        .withBody(getJsonFullMachineDto())
                )
        );

        FullMachineDTO expected = TEST_FULL_MACHINE_DTO_1;
        FullMachineDTO actual = machineService.addMachine(TEST_ADD_MACHINE_DTO_1);

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

    @Test
    public void testMachineExists_returnsTrue() {
        wireMockServer.stubFor(get(urlEqualTo("/test-machines/machines/exist/" + TEST_FULL_MACHINE_DTO_1.getSerialNumber()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                )
        );

        boolean actual = machineService.machineExists(TEST_FULL_MACHINE_DTO_1.getSerialNumber());

        Assertions.assertTrue(actual);
    }

    @Test
    public void testMachineExists_returnsFalse() {
        wireMockServer.stubFor(get(urlEqualTo("/test-machines/machines/exist/" + TEST_FULL_MACHINE_DTO_1.getSerialNumber()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")
                )
        );

        boolean actual = machineService.machineExists(TEST_FULL_MACHINE_DTO_1.getSerialNumber());

        Assertions.assertFalse(actual);
    }

    @Test
    public void testUpdateMachine() {
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

        wireMockServer.stubFor(get(urlEqualTo("/test-machines/machines/exist/" + expected.getSerialNumber()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                )
        );

        wireMockServer.stubFor(put(urlEqualTo("/test-machines/machines/edit/" + TEST_FULL_MACHINE_DTO_1.getId()))
                .withHeader("Content-Type", equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(gson.toJson(expected)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(gson.toJson(expected))
                )
        );

        FullMachineDTO actual = machineService.updateMachine(expected.getId(), expected);

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

    private String getJsonFullMachineDto() {
        return gson.toJson(TEST_FULL_MACHINE_DTO_1);
    }

    private String getJsonAddMachineDto() {
        return gson.toJson(TEST_ADD_MACHINE_DTO_1);
    }

    private String getJsonCustomPagedModel() {
        List<ShortMachineDTO> content = List.of(TEST_SHORT_MACHINE_DTO_1, TEST_SHORT_MACHINE_DTO_2);
        PagedModel.PageMetadata page = new PagedModel.PageMetadata(2, 0, 2, 1);

        return gson.toJson(new CustomPagedModel<>(content, page));
    }
}


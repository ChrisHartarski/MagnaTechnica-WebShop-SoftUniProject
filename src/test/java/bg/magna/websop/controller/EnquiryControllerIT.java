package bg.magna.websop.controller;

import bg.magna.websop.model.dto.enquiry.FullEnquiryDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.entity.Enquiry;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.repository.EnquiryRepository;
import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.MachineService;
import bg.magna.websop.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EnquiryControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @MockBean
    private MachineService machineService;

    @AfterEach
    public void tearDown() {
        enquiryRepository.deleteAll();
        userRepository.deleteAll();

        userService.addAdminUser();
        userService.addFirstUser();
    }

    @Test
    public void testViewAddEnquiryScreen() throws Exception {
        UserEntity user = getUserRoleUser();
        FullMachineDTO testMachineDTO = createTestFullMachineDTO("machineId");

        when(machineService.getById(testMachineDTO.getId())).thenReturn(testMachineDTO);

        mockMvc.perform(get("/machines/enquiries/" + testMachineDTO.getId())
                        .with(user(user.getEmail()).roles("USER"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-machine-enquiry"));
    }

    @Test
    public void testAddEnquiry() throws Exception {
        UserEntity user = getUserRoleUser();
        FullMachineDTO testMachineDTO = createTestFullMachineDTO("machineId");

        Enquiry expected = createTestEnquiry(user);

        mockMvc.perform(post("/machines/enquiries/" + testMachineDTO.getId())
                        .with(user(user.getEmail()))
                        .with(csrf())
                        .param("title", expected.getTitle())
                        .param("message", expected.getMessage()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/machines"));

        Enquiry actual = enquiryRepository.findAll().getFirst();

        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(0L, actual.getId());
        Assertions.assertEquals(expected.getMachineId(), actual.getMachineId());
        Assertions.assertEquals(expected.getUser().getId(), actual.getUser().getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getMessage(), actual.getMessage());
        Assertions.assertNotNull(actual.getCreatedOn());
    }

    @Test
    public void testAddEnquiry_returnsWhenInputIncorrect() throws Exception {
        UserEntity user = getUserRoleUser();
        FullMachineDTO testMachineDTO = createTestFullMachineDTO("machineId");

        when(machineService.getById(testMachineDTO.getId())).thenReturn(testMachineDTO);

        mockMvc.perform(post("/machines/enquiries/" + testMachineDTO.getId())
                        .with(user(user.getEmail()))
                        .with(csrf())
                        .param("title", "")
                        .param("message", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/machines/enquiries/" + testMachineDTO.getId()))
                .andExpect(flash().attribute("fieldsHaveErrors", true));
    }

    @Test
    public void testViewAllEnquiries() throws Exception {
        UserEntity user = getAdminUser();

        FullMachineDTO testMachineDTO = createTestFullMachineDTO("machineId");
        when(machineService.getById(testMachineDTO.getId())).thenReturn(testMachineDTO);

        Enquiry testEnquiry = createTestEnquiryAndSaveToDB(user);
        FullEnquiryDTO fullEnquiryDTO = modelMapper.map(testEnquiry, FullEnquiryDTO.class);

        mockMvc.perform(get("/machines/enquiries/all")
                        .with(user(user.getEmail()).roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("enquiries"))
                .andExpect(model().attributeExists("enquiries"));
    }

    @Test
    public void testViewEnquiryDetails() throws Exception {
        UserEntity user = getAdminUser();

        FullMachineDTO testMachineDTO = createTestFullMachineDTO("machineId");
        when(machineService.getById(testMachineDTO.getId())).thenReturn(testMachineDTO);

        Enquiry expected = createTestEnquiryAndSaveToDB(user);

        mockMvc.perform(get("/machines/enquiries/view/" + expected.getId())
                        .with(user(user.getEmail()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("enquiry-details"))
                .andExpect(model().attributeExists("enquiry"));
    }

    @Test
    public void testDeleteEnquiry() throws Exception {
        UserEntity user = getAdminUser();

        FullMachineDTO testMachineDTO = createTestFullMachineDTO("machineId");
        when(machineService.getById(testMachineDTO.getId())).thenReturn(testMachineDTO);

        Enquiry expected = createTestEnquiryAndSaveToDB(user);

        Assertions.assertEquals(1, enquiryRepository.count());

        mockMvc.perform(delete("/machines/enquiries/delete/" + expected.getId())
                        .with(user(user.getEmail()).roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/machines/enquiries/all"));

        Assertions.assertEquals(0, enquiryRepository.count());
    }


    private Enquiry createTestEnquiry(UserEntity user) {
        return new Enquiry(
                "machineId",
                user,
                "testTitle",
                "testMessage",
                LocalDateTime.now());
    }

    private Enquiry createTestEnquiryAndSaveToDB(UserEntity user) {
        Enquiry enquiry = createTestEnquiry(user);
        return enquiryRepository.saveAndFlush(enquiry);
    }

    private UserEntity getAdminUser(){
        return userRepository.findByEmail("admin@example.com").orElse(null);
    }

    private UserEntity getUserRoleUser(){
        return userRepository.findByEmail("user01@example.com").orElse(null);
    }

    private FullMachineDTO createTestFullMachineDTO(String id) {
        return new FullMachineDTO(id, "serial", "name", "https://imageUrl.com", 2020, "brandName", "descriptionEn", "descriptionBg", 0, 0, 0, "moreInfoEn", "moreInfoBg");
    }
}

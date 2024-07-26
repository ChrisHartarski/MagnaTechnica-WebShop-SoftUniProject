package bg.magna.websop.service.impl;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.dto.enquiry.AddEnquiryDTO;
import bg.magna.websop.model.dto.enquiry.FullEnquiryDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.entity.Company;
import bg.magna.websop.model.entity.Enquiry;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.EnquiryRepository;
import bg.magna.websop.service.MachineService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.service.exception.ResourceNotFoundException;
import bg.magna.websop.service.helper.UserHelperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EnquiryServiceImplTests {
    private EnquiryServiceImpl toTest;
    private static final FullMachineDTO TEST_MACHINE_DTO = new FullMachineDTO("machineId1", "machine1serial", "machine1Name", "machine1URL", 2020, "brand1", "descEn1", "descBg1", 0, 0, 0, "moreInfoEn1", "moreInfoBg1");
    private static final UserEntity TEST_USER = new UserEntity("userId1", "user1@example.com", "some password", "user1", "Name", "some phone", UserRole.USER, new HashMap<>(), new ArrayList<>(), new Company());
    private static final Enquiry TEST_ENQUIRY_1 = new Enquiry(1, TEST_MACHINE_DTO.getId(), TEST_USER, "Enquiry for " + TEST_MACHINE_DTO.getDescriptionEn(), "message1", LocalDateTime.of(2024, 7, 22, 12, 30));
    private static final Enquiry TEST_ENQUIRY_2 = new Enquiry(2, TEST_MACHINE_DTO.getId(), TEST_USER, "Enquiry for " + TEST_MACHINE_DTO.getDescriptionEn(), "message2", LocalDateTime.of(2024, 7, 23, 12, 30));
    private static final AddEnquiryDTO TEST_ADD_ENQUIRY_DTO = new AddEnquiryDTO(TEST_MACHINE_DTO.getId(), TEST_USER.getId(), TEST_USER.getEmail(), TEST_USER.getFullName(), "Enquiry for " + TEST_MACHINE_DTO.getDescriptionEn(), "message1");
    private static final AddEnquiryDTO TEST_ADD_ENQUIRY_DTO_BG = new AddEnquiryDTO(TEST_MACHINE_DTO.getId(), TEST_USER.getId(), TEST_USER.getEmail(), TEST_USER.getFullName(), "Запитване за " + TEST_MACHINE_DTO.getDescriptionBg(), "message1");
    private static final FullEnquiryDTO TEST_FULL_ENQUIRY_DTO_1 = new FullEnquiryDTO(1, TEST_USER.getFullName(), TEST_USER.getEmail(), TEST_MACHINE_DTO.getName(), TEST_MACHINE_DTO.getImageURL(), TEST_MACHINE_DTO.getId(), TEST_MACHINE_DTO.getSerialNumber(), LocalDateTime.of(2024, 7, 22, 12, 30), "Enquiry for " + TEST_MACHINE_DTO.getDescriptionEn(), "message1");
    private static final FullEnquiryDTO TEST_FULL_ENQUIRY_DTO_2 = new FullEnquiryDTO(2, TEST_USER.getFullName(), TEST_USER.getEmail(), TEST_MACHINE_DTO.getName(), TEST_MACHINE_DTO.getImageURL(), TEST_MACHINE_DTO.getId(), TEST_MACHINE_DTO.getSerialNumber(), LocalDateTime.of(2024, 7, 23, 12, 30), "Enquiry for " + TEST_MACHINE_DTO.getDescriptionEn(), "message2");

    @Mock
    private EnquiryRepository enquiryRepository;
    @Mock
    private UserService userService;
    @Mock
    private MachineService machineService;
    @Mock
    private UserHelperService userHelperService;

    @Captor
    private ArgumentCaptor<Enquiry> enquiryCaptor;

    @BeforeEach
    void setUp() {
        toTest = new EnquiryServiceImpl(enquiryRepository, userService, machineService, userHelperService);
    }

    @Test
    void testAddEnquiry() {
        Enquiry expected = TEST_ENQUIRY_1;
        when(userService.getUserById(TEST_USER.getId())).thenReturn(TEST_USER);

        toTest.addEnquiry(TEST_ADD_ENQUIRY_DTO);
        verify(enquiryRepository).saveAndFlush(enquiryCaptor.capture());
        Enquiry actual = enquiryCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getMachineId(),actual.getMachineId());
        Assertions.assertEquals(expected.getUser().getId(),actual.getUser().getId());
        Assertions.assertEquals(expected.getTitle(),actual.getTitle());
        Assertions.assertEquals(expected.getMessage(),actual.getMessage());
        Assertions.assertNotNull(actual.getCreatedOn());
    }

    @Test
    void testGetAddEnquiryDTO() {
        AddEnquiryDTO expected = TEST_ADD_ENQUIRY_DTO;
        when(machineService.getById(TEST_MACHINE_DTO.getId())).thenReturn(TEST_MACHINE_DTO);
        when(userHelperService.getCurrentUserDetails()).thenReturn(new CurrentUserDetails(TEST_USER.getEmail(), TEST_USER.getPassword(), List.of(), TEST_USER.getId(), TEST_USER.getFirstName(), TEST_USER.getLastName(), "companyName", TEST_USER.getCartSize()));
        when(userHelperService.getCurrentUserLocale()).thenReturn(Locale.of("en", "US"));

        AddEnquiryDTO actual = toTest.getAddEnquiryDTO(TEST_MACHINE_DTO.getId());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getMachineId(),actual.getMachineId());
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
        Assertions.assertEquals(expected.getUserEmail(), actual.getUserEmail());
        Assertions.assertEquals(expected.getUserFullName(), actual.getUserFullName());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertNull(actual.getMessage());
    }

    @Test
    void testGetAddEnquiryDTO_withBGLocale() {
        AddEnquiryDTO expected = TEST_ADD_ENQUIRY_DTO_BG;
        when(machineService.getById(TEST_MACHINE_DTO.getId())).thenReturn(TEST_MACHINE_DTO);
        when(userHelperService.getCurrentUserDetails()).thenReturn(new CurrentUserDetails(TEST_USER.getEmail(), TEST_USER.getPassword(), List.of(), TEST_USER.getId(), TEST_USER.getFirstName(), TEST_USER.getLastName(), "companyName", TEST_USER.getCartSize()));
        when(userHelperService.getCurrentUserLocale()).thenReturn(Locale.of("bg", "BG"));

        AddEnquiryDTO actual = toTest.getAddEnquiryDTO(TEST_MACHINE_DTO.getId());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getMachineId(),actual.getMachineId());
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
        Assertions.assertEquals(expected.getUserEmail(), actual.getUserEmail());
        Assertions.assertEquals(expected.getUserFullName(), actual.getUserFullName());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertNull(actual.getMessage());
    }

    @Test
    void testGetAllEnquiries() {
        List<FullEnquiryDTO> expected = List.of(TEST_FULL_ENQUIRY_DTO_1, TEST_FULL_ENQUIRY_DTO_2);

        when(enquiryRepository.findAll()).thenReturn(List.of(TEST_ENQUIRY_1, TEST_ENQUIRY_2));
        when(machineService.getById(TEST_MACHINE_DTO.getId())).thenReturn(TEST_MACHINE_DTO);

        List<FullEnquiryDTO> actual = toTest.getAllEnquiries();

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getId(), actual.get(0).getId());
        Assertions.assertEquals(expected.get(0).getUserEmail(), actual.get(0).getUserEmail());
        Assertions.assertEquals(expected.get(0).getUserFullName(), actual.get(0).getUserFullName());
        Assertions.assertEquals(expected.get(0).getMachineId(),actual.get(0).getMachineId());
        Assertions.assertEquals(expected.get(0).getMachineImageUrl(), actual.get(0).getMachineImageUrl());
        Assertions.assertEquals(expected.get(0).getMachineName(), actual.get(0).getMachineName());
        Assertions.assertEquals(expected.get(0).getMachineSerialNumber(), actual.get(0).getMachineSerialNumber());
        Assertions.assertEquals(expected.get(0).getTitle(),actual.get(0).getTitle());
        Assertions.assertEquals(expected.get(0).getMessage(),actual.get(0).getMessage());
        Assertions.assertEquals(expected.get(0).getCreatedOn(),actual.get(0).getCreatedOn());
        Assertions.assertEquals(expected.get(1).getId(), actual.get(1).getId());
        Assertions.assertEquals(expected.get(1).getUserEmail(), actual.get(1).getUserEmail());
        Assertions.assertEquals(expected.get(1).getUserFullName(), actual.get(1).getUserFullName());
        Assertions.assertEquals(expected.get(1).getMachineId(),actual.get(1).getMachineId());
        Assertions.assertEquals(expected.get(1).getMachineImageUrl(), actual.get(1).getMachineImageUrl());
        Assertions.assertEquals(expected.get(1).getMachineName(), actual.get(1).getMachineName());
        Assertions.assertEquals(expected.get(1).getMachineSerialNumber(), actual.get(1).getMachineSerialNumber());
        Assertions.assertEquals(expected.get(1).getTitle(),actual.get(1).getTitle());
        Assertions.assertEquals(expected.get(1).getMessage(),actual.get(1).getMessage());
        Assertions.assertEquals(expected.get(1).getCreatedOn(),actual.get(1).getCreatedOn());
    }

    @Test
    void testGetById() {
        FullEnquiryDTO expected = TEST_FULL_ENQUIRY_DTO_1;

        when(enquiryRepository.findById(TEST_ENQUIRY_1.getId())).thenReturn(Optional.of(TEST_ENQUIRY_1));
        when(machineService.getById(TEST_MACHINE_DTO.getId())).thenReturn(TEST_MACHINE_DTO);

        FullEnquiryDTO actual = toTest.getById(TEST_ENQUIRY_1.getId());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getUserEmail(), actual.getUserEmail());
        Assertions.assertEquals(expected.getUserFullName(), actual.getUserFullName());
        Assertions.assertEquals(expected.getMachineId(),actual.getMachineId());
        Assertions.assertEquals(expected.getMachineImageUrl(), actual.getMachineImageUrl());
        Assertions.assertEquals(expected.getMachineName(), actual.getMachineName());
        Assertions.assertEquals(expected.getMachineSerialNumber(), actual.getMachineSerialNumber());
        Assertions.assertEquals(expected.getTitle(),actual.getTitle());
        Assertions.assertEquals(expected.getMessage(),actual.getMessage());
        Assertions.assertEquals(expected.getCreatedOn(),actual.getCreatedOn());
    }

    @Test
    void testGetById_throwsException() {
        when(enquiryRepository.findById(TEST_ENQUIRY_1.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> toTest.getById(TEST_ENQUIRY_1.getId()));
    }
}

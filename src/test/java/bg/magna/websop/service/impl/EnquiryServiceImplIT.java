package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.entity.Enquiry;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.repository.EnquiryRepository;
import bg.magna.websop.service.EnquiryService;
import bg.magna.websop.service.MachineService;
import bg.magna.websop.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EnquiryServiceImplIT {
    private static final FullMachineDTO TEST_MACHINE_DTO = new FullMachineDTO("machineId1", "machine1serial", "machine1Name", "machine1URL", 2020, "brand1", "descEn1", "descBg1", 0, 0, 0, "moreInfoEn1", "moreInfoBg1");

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private EnquiryService enquiryService;

    @Autowired
    private UserService userService;

    @MockBean
    private MachineService machineService;

    @AfterEach
    public void tearDown() {
        enquiryRepository.deleteAll();
    }

    @Test
    public void testDeleteOldEnquiries() {
        when(machineService.getById("machineId")).thenReturn(TEST_MACHINE_DTO);
        FullMachineDTO machineDTO = machineService.getById("machineId");
        UserEntity user = userService.getUserByEmail("user01@example.com");

        Assertions.assertEquals(0, enquiryRepository.count());

        Enquiry enquiry1 = createTestEnquiry(1, machineDTO, user);
        Enquiry enquiry2 = createTestEnquiry(2, machineDTO, user);
        Enquiry enquiry3 = createTestEnquiry(3, machineDTO, user);
        enquiry3.setCreatedOn(LocalDateTime.now());
        enquiryRepository.saveAllAndFlush(List.of(enquiry1, enquiry2, enquiry3));

        Assertions.assertEquals(3, enquiryRepository.count());

        enquiryService.deleteOldEnquiries();

        Assertions.assertEquals(1, enquiryRepository.count());
    }

    private Enquiry createTestEnquiry(long id, FullMachineDTO machineDTO, UserEntity user) {
        return new Enquiry (
                id,
                machineDTO.getId(),
                user,
                "Enquiry for " + machineDTO.getDescriptionEn(),
                "message1",
                LocalDateTime.of(2022, 7, 22, 12, 30));
    }

}

package bg.magna.websop.service.impl;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.dto.AddEnquiryDTO;
import bg.magna.websop.model.dto.FullMachineDTO;
import bg.magna.websop.model.entity.Enquiry;
import bg.magna.websop.repository.EnquiryRepository;
import bg.magna.websop.service.EnquiryService;
import bg.magna.websop.service.MachineService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.service.helper.UserHelperService;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EnquiryServiceImpl implements EnquiryService {
    private final EnquiryRepository enquiryRepository;
    private final UserService userService;
    private final MachineService machineService;
    private final UserHelperService userHelperService;

    public EnquiryServiceImpl(EnquiryRepository enquiryRepository, UserService userService, MachineService machineService, UserHelperService userHelperService) {
        this.enquiryRepository = enquiryRepository;
        this.userService = userService;
        this.machineService = machineService;
        this.userHelperService = userHelperService;
    }

    @Override
    public void addEnquiry(AddEnquiryDTO enquiryData) {
        Enquiry enquiry = new Enquiry();
        enquiry.setMachineId(enquiryData.getMachineId());
        enquiry.setUser(userService.getUserById(enquiryData.getUserId()));
        enquiry.setTitle(enquiryData.getTitle());
        enquiry.setMessage(enquiryData.getMessage());

        enquiryRepository.saveAndFlush(enquiry);
    }

    @Override
    public AddEnquiryDTO getAddEnquiryDTO(String machineId) {
        AddEnquiryDTO enquiryData = new AddEnquiryDTO();
        FullMachineDTO machineDTO = machineService.getById(machineId);
        CurrentUserDetails currentUserDetails = userHelperService.getCurrentUserDetails();
        enquiryData.setMachineId(machineId);
        enquiryData.setUserId(currentUserDetails.getId());
        enquiryData.setUserEmail(currentUserDetails.getUsername());
        enquiryData.setUserFullName(currentUserDetails.getFullName());

        Locale currentLocale = userHelperService.getCurrentUserLocale();
        if(currentLocale.toString().equals("en_US")){
            enquiryData.setTitle("Enquiry for " + machineDTO.getDescriptionEn());
        }
        if(currentLocale.toString().equals("bg_BG")){
            enquiryData.setTitle("Запитване за " + machineDTO.getDescriptionBg());
        }

        return enquiryData;
    }
}

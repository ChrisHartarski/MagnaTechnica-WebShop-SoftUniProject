package bg.magna.websop.service.impl;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.dto.enquiry.AddEnquiryDTO;
import bg.magna.websop.model.dto.enquiry.FullEnquiryDTO;
import bg.magna.websop.model.dto.machine.FullMachineDTO;
import bg.magna.websop.model.entity.Enquiry;
import bg.magna.websop.repository.EnquiryRepository;
import bg.magna.websop.service.EnquiryService;
import bg.magna.websop.service.MachineService;
import bg.magna.websop.service.UserService;
import bg.magna.websop.service.helper.UserHelperService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class EnquiryServiceImpl implements EnquiryService {
    private final EnquiryRepository enquiryRepository;
    private final UserService userService;
    private final MachineService machineService;
    private final UserHelperService userHelperService;
    private final ModelMapper modelMapper;

    public EnquiryServiceImpl(EnquiryRepository enquiryRepository, UserService userService, MachineService machineService, UserHelperService userHelperService, ModelMapper modelMapper) {
        this.enquiryRepository = enquiryRepository;
        this.userService = userService;
        this.machineService = machineService;
        this.userHelperService = userHelperService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addEnquiry(AddEnquiryDTO enquiryData) {
        Enquiry enquiry = new Enquiry();
        enquiry.setMachineId(enquiryData.getMachineId());
        enquiry.setUser(userService.getUserById(enquiryData.getUserId()));
        enquiry.setTitle(enquiryData.getTitle());
        enquiry.setMessage(enquiryData.getMessage());
        enquiry.setCreatedOn(LocalDateTime.now());

        enquiryRepository.saveAndFlush(enquiry);
    }

    @Override
    public AddEnquiryDTO getAddEnquiryDTO() {
        AddEnquiryDTO enquiryData = new AddEnquiryDTO();

        CurrentUserDetails currentUserDetails = userHelperService.getCurrentUserDetails();
        enquiryData.setMachineId("");
        enquiryData.setUserId(currentUserDetails.getId());
        enquiryData.setUserEmail(currentUserDetails.getUsername());
        enquiryData.setUserFullName(currentUserDetails.getFullName());

//        Locale currentLocale = userHelperService.getCurrentUserLocale();
//        if(currentLocale.toString().equals("en_US")){
//            enquiryData.setTitle("Enquiry for " + machineDTO.getDescriptionEn());
//        }
//        if(currentLocale.toString().equals("bg_BG")){
//            enquiryData.setTitle("Запитване за " + machineDTO.getDescriptionBg());
//        }

        return enquiryData;
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

    @Override
    public List<FullEnquiryDTO> getAllEnquiries() {
        return enquiryRepository.findAll().stream()
                .map(this::mapEnquiryToDTO)
                .toList();
    }

    @Override
    public FullEnquiryDTO getById(long id) {
        return enquiryRepository.findById(id)
                .map(this::mapEnquiryToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Enquiry with id " + id + " not found"));
    }

    @Override
    public void delete(long id) {
        enquiryRepository.deleteById(id);
    }

    private FullEnquiryDTO mapEnquiryToDTO(Enquiry enquiry) {
        FullMachineDTO machine = machineService.getById(enquiry.getMachineId());

        FullEnquiryDTO dto = new FullEnquiryDTO();
        dto.setId(enquiry.getId());
        dto.setUserFullName(enquiry.getUser().getFullName());
        dto.setUserEmail(enquiry.getUser().getEmail());
        dto.setMachineName(machine.getName());
        dto.setMachineImageUrl(machine.getImageURL());
        dto.setMachineId(machine.getId());
        dto.setMachineSerialNumber(machine.getSerialNumber());
        dto.setCreatedOn(enquiry.getCreatedOn());
        dto.setTitle(enquiry.getTitle());
        dto.setMessage(enquiry.getMessage());

        return dto;
    }
}

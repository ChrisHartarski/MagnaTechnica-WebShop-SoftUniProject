package bg.magna.websop.service;

import bg.magna.websop.model.dto.AddEnquiryDTO;
import bg.magna.websop.model.dto.FullEnquiryDTO;

import java.util.List;

public interface EnquiryService {
    void addEnquiry(AddEnquiryDTO enquiryData);

    AddEnquiryDTO getAddEnquiryDTO();

    AddEnquiryDTO getAddEnquiryDTO(String machineId);

    List<FullEnquiryDTO> getAllEnquiries();
}

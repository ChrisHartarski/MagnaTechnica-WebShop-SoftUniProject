package bg.magna.websop.service;

import bg.magna.websop.model.dto.AddEnquiryDTO;

public interface EnquiryService {
    void addEnquiry(AddEnquiryDTO enquiryData);

    AddEnquiryDTO getAddEnquiryDTO(String machineId);
}

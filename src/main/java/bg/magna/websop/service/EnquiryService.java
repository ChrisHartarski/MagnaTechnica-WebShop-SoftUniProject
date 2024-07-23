package bg.magna.websop.service;

import bg.magna.websop.model.dto.enquiry.AddEnquiryDTO;
import bg.magna.websop.model.dto.enquiry.FullEnquiryDTO;

import java.util.List;

public interface EnquiryService {
    void addEnquiry(AddEnquiryDTO enquiryData);

    AddEnquiryDTO getAddEnquiryDTO(String machineId);

    List<FullEnquiryDTO> getAllEnquiries();

    FullEnquiryDTO getById(long id);

    void delete(long id);
}

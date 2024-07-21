package bg.magna.websop.service.impl;

import bg.magna.websop.repository.EnquiryRepository;
import bg.magna.websop.service.EnquiryService;
import org.springframework.stereotype.Service;

@Service
public class EnquiryServiceImpl implements EnquiryService {
    private final EnquiryRepository enquiryRepository;

    public EnquiryServiceImpl(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }
}

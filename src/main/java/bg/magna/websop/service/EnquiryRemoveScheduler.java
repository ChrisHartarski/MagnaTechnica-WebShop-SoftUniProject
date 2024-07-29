package bg.magna.websop.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnquiryRemoveScheduler {
    private final EnquiryService enquiryService;

    public EnquiryRemoveScheduler(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @Scheduled(cron = "0 0 5 * * *")
    public void deleteOldEnquiries(){
        enquiryService.deleteOldEnquiries();
    }
}

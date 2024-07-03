package bg.magna.websop.service.impl;

import bg.magna.websop.repository.PartOrderRepository;
import bg.magna.websop.service.PartOrderService;
import org.springframework.stereotype.Service;

@Service
public class PartOrderServiceImpl implements PartOrderService {
    private final PartOrderRepository partOrderRepository;

    public PartOrderServiceImpl(PartOrderRepository partOrderRepository) {
        this.partOrderRepository = partOrderRepository;
    }
}

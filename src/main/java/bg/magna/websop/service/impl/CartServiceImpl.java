package bg.magna.websop.service.impl;

import bg.magna.websop.repository.CartRepository;
import bg.magna.websop.service.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
}

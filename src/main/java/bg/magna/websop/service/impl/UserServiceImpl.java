package bg.magna.websop.service.impl;

import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

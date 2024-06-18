package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.RegisterUserDTO;
import bg.magna.websop.model.entity.User;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean userRepositoryIsEmpty() {
        return userRepository.count() == 0;
    }

    @Override
    public boolean userEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void saveUserToDB(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void addAdminUser() {
        saveUserToDB(new User("admin@mail", "asdasd", "admin", "admin", UserRole.ADMIN));
    }

    @Override
    public void registerUser(RegisterUserDTO registerData) {
        User user = modelMapper.map(registerData, User.class);
        user.setUserRole(UserRole.USER);
        saveUserToDB(user);
    }


}

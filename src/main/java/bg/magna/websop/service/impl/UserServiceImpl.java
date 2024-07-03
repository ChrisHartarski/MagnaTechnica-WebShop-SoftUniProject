package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.LoginUserDTO;
import bg.magna.websop.model.dto.RegisterUserDTO;
import bg.magna.websop.model.entity.User;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.UserService;
import bg.magna.websop.util.UserSession;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserSession userSession;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, UserSession userSession) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.userSession = userSession;
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
    public void addFirstUser() {
        saveUserToDB(new User("user01@mail", "asdasd", "user01", "user01", UserRole.USER));
    }

    @Override
    public void registerUser(RegisterUserDTO registerData) {
        User user = modelMapper.map(registerData, User.class);
        if(user.getUserRole() == null) {
            user.setUserRole(UserRole.USER);
        }
        saveUserToDB(user);
    }

    @Override
    public boolean isValidUser(LoginUserDTO loginData) {
        return getUserByEmailAndPassword(loginData) != null;
    }

    @Override
    public void loginUser(LoginUserDTO loginData) {
        userSession.login(getUserByEmailAndPassword(loginData));
    }

    @Override
    public void logoutUser() {
        userSession.logout();
    }

    @Override
    public long getUserCount() {
        return userRepository.count();
    }

    @Override
    public long getUserCountByRole(UserRole userRole) {
        return userRepository.findAllByUserRole(userRole).size();
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    private User getUserByEmailAndPassword(LoginUserDTO loginData) {
        return userRepository.getUserByEmail(loginData.getEmail())
                .filter(u -> passwordEncoder.matches(loginData.getPassword(), u.getPassword()))
                .orElse(null);
    }
}

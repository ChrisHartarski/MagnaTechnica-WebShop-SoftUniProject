package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.*;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.CompanyService;
import bg.magna.websop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserSession userSession;

    public UserServiceImpl(UserRepository userRepository, CompanyService companyService, PasswordEncoder passwordEncoder, ModelMapper modelMapper, UserSession userSession) {
        this.userRepository = userRepository;
        this.companyService = companyService;
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
    public void saveUserToDB(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);

    }

    @Override
    public void addAdminUser() {
        saveUserToDB(new UserEntity("admin@mail", "asdasd", "admin", "admin", UserRole.ADMIN, companyService.getCompanyByName("Magna Technica Ltd.")));
    }

    @Override
    public void addFirstUser() {
        saveUserToDB(new UserEntity("user01@mail", "asdasd", "user01", "user01", UserRole.USER, companyService.getCompanyByName("Company 1")));
    }

    @Override
    public void registerUser(UserDTO registerData) {
        UserEntity user = modelMapper.map(registerData, UserEntity.class);
        if(user.getUserRole() == null) {
            user.setUserRole(UserRole.USER);
        }
        user.setCompany(companyService.getCompanyByName(registerData.getCompanyName()));
        saveUserToDB(user);
    }

    @Override
    public boolean isValidUser(ValidateUserDTO loginData) {
        return getUserByEmailAndPassword(loginData) != null;
    }

    @Override
    public void loginUser(ValidateUserDTO loginData) {
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
    public UserEntity getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDTO getCurrentUserData() {
        UserEntity user = userRepository.getReferenceById(userSession.getId());
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void editUserData(EditUserDTO userData) {
        UserEntity user = userRepository.getReferenceById(userSession.getId());
        modelMapper.map(userData, user);
        user.setCompany(companyService.getCompanyByName(userData.getCompanyName()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUserEmail(UserEmailDTO userData) {
        UserEntity user = userRepository.getReferenceById(userSession.getId());
        modelMapper.map(userData, user);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUserPassword(UserPasswordDTO userData) {
        UserEntity user = userRepository.getReferenceById(userSession.getId());
        user.setPassword(userData.getPassword());
        saveUserToDB(user);
    }

    private UserEntity getUserByEmailAndPassword(ValidateUserDTO loginData) {
        return userRepository.getUserByEmail(loginData.getEmail())
                .filter(u -> passwordEncoder.matches(loginData.getPassword(), u.getPassword()))
                .orElse(null);
    }
}

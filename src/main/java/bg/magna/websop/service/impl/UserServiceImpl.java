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

    public UserServiceImpl(UserRepository userRepository, CompanyService companyService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.companyService = companyService;
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
    public void encodePassAndSaveUserToDB(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        saveUserToDB(user);

    }

    @Override
    public void saveUserToDB(UserEntity user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    public void addAdminUser() {
        encodePassAndSaveUserToDB(new UserEntity("admin@mail", "asdasd", "admin", "admin", UserRole.ADMIN, companyService.getCompanyByName("Magna Technica Ltd.")));
    }

    @Override
    public void addFirstUser() {
        encodePassAndSaveUserToDB(new UserEntity("user01@mail", "asdasd", "user01", "user01", UserRole.USER, companyService.getCompanyByName("Company 1")));
    }

    @Override
    public void registerUser(UserDTO registerData) {
        UserEntity user = modelMapper.map(registerData, UserEntity.class);
        if(user.getUserRole() == null) {
            user.setUserRole(UserRole.USER);
        }
        user.setCompany(companyService.getCompanyByName(registerData.getCompanyName()));
        encodePassAndSaveUserToDB(user);
    }

    @Override
    public boolean isValidUser(ValidateUserDTO loginData) {
        return getUserByEmailAndPassword(loginData) != null;
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
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with id: " + id + ", does not exist!"));
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User with email: " + email + ", does not exist!"));
    }

    @Override
    public UserDTO getCurrentUserData(String id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such user exists!"));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void editUserData(EditUserDTO userData, String userId) {
        UserEntity user = getUserById(userId);
        modelMapper.map(userData, user);
        user.setCompany(companyService.getCompanyByName(userData.getCompanyName()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUserEmail(UserEmailDTO userData, String userId) {
        UserEntity user = getUserById(userId);
        modelMapper.map(userData, user);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUserPassword(UserPasswordDTO userData, String userId) {
        UserEntity user = getUserById(userId);
        user.setPassword(userData.getPassword());
        encodePassAndSaveUserToDB(user);
    }

    @Override
    public void emptyUserCart(UserEntity user) {
        user.emptyCart();
        saveUserToDB(user);
    }

    private UserEntity getUserByEmailAndPassword(ValidateUserDTO loginData) {
        return userRepository.getUserByEmail(loginData.getEmail())
                .filter(u -> passwordEncoder.matches(loginData.getPassword(), u.getPassword()))
                .orElse(null);
    }
}

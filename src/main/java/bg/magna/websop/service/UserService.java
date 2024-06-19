package bg.magna.websop.service;

import bg.magna.websop.model.dto.LoginUserDTO;
import bg.magna.websop.model.dto.RegisterUserDTO;
import bg.magna.websop.model.entity.User;

public interface UserService {
    boolean userRepositoryIsEmpty();
    boolean userEmailExists(String email);
    void saveUserToDB(User user);
    void addAdminUser();
    void registerUser(RegisterUserDTO registerData);
    boolean isValidUser(LoginUserDTO loginData);
    void loginUser(LoginUserDTO loginData);
    void logoutUser();
}

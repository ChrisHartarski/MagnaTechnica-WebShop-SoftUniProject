package bg.magna.websop.service;

import bg.magna.websop.model.dto.*;
import bg.magna.websop.model.entity.User;
import bg.magna.websop.model.enums.UserRole;

public interface UserService {
    boolean userRepositoryIsEmpty();
    boolean userEmailExists(String email);
    void saveUserToDB(User user);
    void addAdminUser();
    void addFirstUser();
    void registerUser(UserDTO registerData);
    boolean isValidUser(ValidateUserDTO loginData);
    void loginUser(ValidateUserDTO loginData);
    void logoutUser();
    long getUserCount();
    long getUserCountByRole(UserRole userRole);
    User getUserById(String id);
    UserDTO getCurrentUserData();
    void editUserData(EditUserDTO userData);
    void updateUserEmail(UserEmailDTO userData);
    void updateUserPassword(UserPasswordDTO userData);
}

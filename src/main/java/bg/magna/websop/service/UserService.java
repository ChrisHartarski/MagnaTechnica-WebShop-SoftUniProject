package bg.magna.websop.service;

import bg.magna.websop.model.dto.*;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;

public interface UserService {
    boolean userRepositoryIsEmpty();
    boolean userEmailExists(String email);
    void encodePassAndSaveUserToDB(UserEntity user);
    void saveUserToDB(UserEntity user);
    void addAdminUser();
    void addFirstUser();
    void registerUser(UserDTO registerData);
    boolean isValidUser(ValidateUserDTO loginData);
    long getUserCount();
    long getUserCountByRole(UserRole userRole);
    UserEntity getUserById(String id);
    UserEntity getUserByEmail(String email);
    UserDTO getCurrentUserData(String id);
    void editUserData(EditUserDTO userData, String userId);
    void updateUserEmail(UserEmailDTO userData, String userId);
    void updateUserPassword(UserPasswordDTO userData, String userId);

    void emptyUserCart(UserEntity user);
}

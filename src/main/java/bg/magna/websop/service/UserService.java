package bg.magna.websop.service;

import bg.magna.websop.model.dto.user.*;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;

public interface UserService {
    boolean userRepositoryIsEmpty();

    boolean userEmailExists(String email);

    void saveUserToDB(UserEntity user);

    void encodePassAndSaveUserToDB(UserEntity user);

    void addAdminUser();

    void addFirstUser();

    void registerUser(UserDTO registerData);

    boolean isValidUser(ValidateUserDTO loginData);

    long getUserCount();

    long getUserCountByRole(UserRole userRole);

    UserEntity getUserById(String id);

    UserEntity getUserByEmail(String email);

    UserDTO getCurrentUserData(String email);

    void editUserData(EditUserDTO userData, String userEmail);

    void editUserEmail(UserEmailDTO userData, String userId);

    void editUserPassword(UserPasswordDTO userData, String userId);

    void emptyUserCart(UserEntity user);
}

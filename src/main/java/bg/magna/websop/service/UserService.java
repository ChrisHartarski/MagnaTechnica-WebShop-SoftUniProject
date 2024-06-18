package bg.magna.websop.service;

import bg.magna.websop.model.entity.User;

public interface UserService {
    boolean userRepositoryIsEmpty();
    void saveUser(User user);
    void addAdminUser();
}

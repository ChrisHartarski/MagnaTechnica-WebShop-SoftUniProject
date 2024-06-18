package bg.magna.websop.util;

import bg.magna.websop.model.entity.User;
import bg.magna.websop.model.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {
    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private UserRole userRole;

    public boolean isUserLoggedIn() {
        return id.isEmpty();
    }

    public void login(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userRole = user.getUserRole();
    }

    public void logout(){
        this.id = "";
        this.firstName = "";
        this.lastName = "";
        this.userRole = UserRole.USER;
    };

    public boolean isAdmin() {
        return getUserRole().equals(UserRole.ADMIN);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRole getUserRole() {
        return userRole;
    }
}

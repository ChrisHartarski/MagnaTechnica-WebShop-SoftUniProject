package bg.magna.websop.util;

import bg.magna.websop.model.entity.User;
import bg.magna.websop.model.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {
    private String id = "";
    private String email = "";
    private String firstName = "";
    private String lastName = "";
    private String companyName = "";
    private UserRole userRole = UserRole.USER;
    private Cart cart;

    public boolean isUserLoggedIn() {
        return !getId().isEmpty();
    }

    public boolean isUserWithUserRoleLoggedIn() {
        if(isUserLoggedIn()) {
            return getUserRole().equals(UserRole.USER);
        }
        return false;
    }

    public boolean isAdminLoggedIn() {
        if(isUserLoggedIn()) {
            return getUserRole().equals(UserRole.ADMIN);
        }
        return false;
    }

    @Transactional
    public void login(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.companyName = user.getCompany().getName();
        this.userRole = user.getUserRole();
        this.cart = new Cart();
    }

    public void logout(){
        this.id = "";
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.companyName = "";
        this.userRole = UserRole.USER;
        this.cart = new Cart();
    };

    public boolean isAdmin() {
        return getUserRole().equals(UserRole.ADMIN);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {return getFirstName() + " " + getLastName();}

    public String getCompanyName() {
        return companyName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public Cart getCart() {
        return cart;
    }
}

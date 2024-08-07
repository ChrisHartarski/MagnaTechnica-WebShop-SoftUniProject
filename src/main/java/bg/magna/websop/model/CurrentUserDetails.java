package bg.magna.websop.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CurrentUserDetails extends User {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String companyName;
    private final int cartSize;

    public CurrentUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities, String id, String firstName, String lastName, String companyName, int cartSize) {
        super(email, password, authorities);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.cartSize = cartSize;
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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getCartSize() {
        return cartSize;
    }

    public boolean isAdmin() {
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }
}

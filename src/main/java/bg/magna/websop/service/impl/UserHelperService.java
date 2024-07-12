package bg.magna.websop.service.impl;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHelperService {
    private final UserService userService;

    public UserHelperService(UserService userService) {
        this.userService = userService;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public CurrentUserDetails getCurrentUserDetails() {
        UserDetails userDetails = (UserDetails) getAuthentication().getPrincipal();
        UserEntity user = userService.getUserByEmail(userDetails.getUsername());
        return mapUserToUserDetails(user);
    }

    private static CurrentUserDetails mapUserToUserDetails(UserEntity user) {
        return new CurrentUserDetails(
                user.getEmail(),
                user.getPassword(),
                List.of(mapUserRoleToGrantedAuthority(user.getUserRole())),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getCompany().getName(),
                user.getCartSize()
        );
    }

    private static GrantedAuthority mapUserRoleToGrantedAuthority(UserRole userRole) {
        return new SimpleGrantedAuthority("ROLE_" + userRole);
    }
}

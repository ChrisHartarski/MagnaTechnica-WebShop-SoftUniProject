package bg.magna.websop.service.helper;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.service.UserService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

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

    public void updateAuthentication(String userId) {
        CurrentUserDetails userDetails = mapUserToUserDetails(userService.getUserById(userId));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Locale getCurrentUserLocale() {
        return LocaleContextHolder.getLocale();
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

package bg.magna.websop.service.impl;

import bg.magna.websop.model.MagnaUserDetails;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class MagnaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public MagnaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(MagnaUserDetailsService::mapUserToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found!"));
    }

    private static MagnaUserDetails mapUserToUserDetails(UserEntity user) {
        return new MagnaUserDetails(
                user.getEmail(),
                user.getPassword(),
                List.of(mapUserRoleToGrantedAuthority(user.getUserRole())),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getCompany().getName()
        );
    }

    private static GrantedAuthority mapUserRoleToGrantedAuthority(UserRole userRole) {
        return new SimpleGrantedAuthority("ROLE_" + userRole);
    }
}

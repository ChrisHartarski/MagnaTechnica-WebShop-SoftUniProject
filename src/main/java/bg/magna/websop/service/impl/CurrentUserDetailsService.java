package bg.magna.websop.service.impl;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CurrentUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CurrentUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(CurrentUserDetailsService::mapUserToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found!"));
    }

    @Transactional
    public CurrentUserDetails loadCurrentUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(CurrentUserDetailsService::mapUserToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found!"));
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

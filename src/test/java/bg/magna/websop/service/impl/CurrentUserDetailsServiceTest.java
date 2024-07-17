package bg.magna.websop.service.impl;

import bg.magna.websop.model.CurrentUserDetails;
import bg.magna.websop.model.entity.Company;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrentUserDetailsServiceTest {
    private CurrentUserDetailsService toTest;
    private final static Company TEST_COMPANY = new Company("TestCompany", "TestVAT", "TestAddress", "TestPhone", "testCompany@example.com");
    private final static UserEntity TEST_USER = new UserEntity("testUser@example.com", "password", "Test", "User", UserRole.USER, TEST_COMPANY);

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        toTest = new CurrentUserDetailsService(userRepository);
    }

    @Test
    void testLoadUserByUsername_userFound() {
        TEST_USER.setId("some UUID");
        TEST_USER.setCart(Map.of(new Part(), 2));

        when(userRepository.findByEmail(TEST_USER.getEmail())).thenReturn(Optional.of(TEST_USER));

        UserDetails actual = toTest.loadUserByUsername(TEST_USER.getEmail());

        Assertions.assertInstanceOf(CurrentUserDetails.class, actual);

        CurrentUserDetails currentUserDetails = (CurrentUserDetails) actual;

        Assertions.assertNotNull(currentUserDetails);
        Assertions.assertEquals(TEST_USER.getEmail(), currentUserDetails.getUsername());
        Assertions.assertEquals(TEST_USER.getPassword(), currentUserDetails.getPassword());
        Assertions.assertEquals(TEST_USER.getId(), currentUserDetails.getId());
        Assertions.assertEquals(TEST_USER.getFirstName(), currentUserDetails.getFirstName());
        Assertions.assertEquals(TEST_USER.getLastName(), currentUserDetails.getLastName());
        Assertions.assertEquals(TEST_USER.getCompany().getName(), currentUserDetails.getCompanyName());
        Assertions.assertEquals(TEST_USER.getCartSize(), currentUserDetails.getCartSize());
    }

    @Test
    void testLoadUserByUsername_userNotFound() {
        when(userRepository.findByEmail(TEST_USER.getEmail())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,() -> toTest.loadUserByUsername(TEST_USER.getEmail()));
    }
}

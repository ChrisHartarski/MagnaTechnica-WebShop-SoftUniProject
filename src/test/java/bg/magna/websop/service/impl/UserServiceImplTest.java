package bg.magna.websop.service.impl;

import bg.magna.websop.model.entity.Company;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.CompanyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private UserServiceImpl toTest;
    private static final Part TEST_PART = new Part("TestPart1", BigDecimal.valueOf(20), 10);
    private static final Company TEST_COMPANY = new Company("TestCompany", "TestVAT", "TestAddress", "TestPhone", "testCompany@example.com");
    private static final UserEntity TEST_USER_1 = new UserEntity("UUID1", "testUser1@example.com", "password1", "Test1", "User1", "0888888888", UserRole.USER, Map.of(TEST_PART, 5), new ArrayList<>(), TEST_COMPANY);
    private static final UserEntity TEST_USER_2 = new UserEntity("UUID2", "testUser2@example.com", "password2", "Test2", "User2", "0999999999", UserRole.USER, Map.of(TEST_PART, 2), new ArrayList<>(), TEST_COMPANY);

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyService companyService;

    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;

    @BeforeEach
    public void setUp() {
        toTest = new UserServiceImpl(userRepository, companyService, new BCryptPasswordEncoder(), new ModelMapper());
    }

    @Test
    void testUserRepositoryIsEmpty() {
        when(userRepository.count()).thenReturn(0L);
        Assertions.assertTrue(toTest.userRepositoryIsEmpty());

        when(userRepository.count()).thenReturn(1L);
        Assertions.assertFalse(toTest.userRepositoryIsEmpty());
    }

    @Test
    void testUserEmailExists(){
        when(userRepository.existsByEmail(TEST_USER_1.getEmail())).thenReturn(true);
        Assertions.assertTrue(toTest.userEmailExists(TEST_USER_1.getEmail()));

        when(userRepository.existsByEmail(TEST_USER_1.getEmail())).thenReturn(false);
        Assertions.assertFalse(toTest.userEmailExists(TEST_USER_1.getEmail()));
    }

    @Test
    void testSaveUserToDB() {
        toTest.saveUserToDB(TEST_USER_1);
        verify(userRepository).saveAndFlush(userCaptor.capture());
        UserEntity actual = userCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_USER_1.getEmail(), actual.getEmail());
        Assertions.assertEquals(TEST_USER_1.getPassword(), actual.getPassword());
        Assertions.assertEquals(TEST_USER_1.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(TEST_USER_1.getLastName(), actual.getLastName());
        Assertions.assertEquals(TEST_USER_1.getPhone(), actual.getPhone());
        Assertions.assertEquals(TEST_USER_1.getCompany(), actual.getCompany());
        Assertions.assertEquals(TEST_USER_1.getUserRole(), actual.getUserRole());
        Assertions.assertEquals(TEST_USER_1.getCartSize(), actual.getCartSize());
    }

    @Test
    void testEncodePassAndSaveToDB_ensurePassIsEncoded() {
        String expected = TEST_USER_1.getPassword();
        toTest.encodePassAndSaveUserToDB(TEST_USER_1);
        verify(userRepository).saveAndFlush(userCaptor.capture());
        UserEntity actual = userCaptor.getValue();

        Assertions.assertNotEquals(expected, actual.getPassword());
    }


}

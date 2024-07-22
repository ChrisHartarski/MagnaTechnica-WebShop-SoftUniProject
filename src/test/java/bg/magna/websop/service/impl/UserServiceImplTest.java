package bg.magna.websop.service.impl;

import bg.magna.websop.model.dto.user.*;
import bg.magna.websop.model.entity.Company;
import bg.magna.websop.model.entity.Part;
import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.CompanyService;
import bg.magna.websop.service.exception.UserNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private UserServiceImpl toTest;
    private static final Part TEST_PART = new Part("TestPart1", BigDecimal.valueOf(20), 10);
    private static final Company TEST_COMPANY = new Company("TestCompany", "TestVAT", "TestAddress", "TestPhone", "testCompany@example.com");
    private static final UserEntity TEST_USER_1 = new UserEntity("UUID1", "testUser1@example.com", "password1", "Test1", "User1", "0888888888", UserRole.USER, Map.of(TEST_PART, 5), new ArrayList<>(), TEST_COMPANY);
    private static final UserEntity TEST_USER_2 = new UserEntity("UUID2", "testUser2@example.com", "password2", "Test2", "User2", "0999999999", UserRole.USER, Map.of(TEST_PART, 2), new ArrayList<>(), TEST_COMPANY);
    private static final UserEntity TEST_USER_3 = new UserEntity("UUID3", "testUser3@example.com", "password3", "Test3", "User3", "0333333333", UserRole.USER, Map.of(TEST_PART, 3), new ArrayList<>(), TEST_COMPANY);
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyService companyService;

    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;

    @BeforeEach
    public void setUp() {
        toTest = new UserServiceImpl(userRepository, companyService, passwordEncoder, new ModelMapper());
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

        Assertions.assertTrue(passwordEncoder.matches(expected, actual.getPassword()));
    }

    @Test
    void testAddAdminUser() {
        Company expectedCompany = new Company("Magna Technica Ltd.", "BG203779968", "address", "phone", "email@example.com");
        when(companyService.getCompanyByName("Magna Technica Ltd.")).thenReturn(expectedCompany);

        toTest.addAdminUser();
        verify(userRepository).saveAndFlush(userCaptor.capture());
        UserEntity actual = userCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("admin@example.com", actual.getEmail());
        Assertions.assertTrue(passwordEncoder.matches("asdasd", actual.getPassword()));
        Assertions.assertEquals(UserRole.ADMIN, actual.getUserRole());
        Assertions.assertEquals("admin", actual.getFirstName());
        Assertions.assertEquals("admin", actual.getLastName());
        Assertions.assertEquals("Magna Technica Ltd.", actual.getCompany().getName());
    }

    @Test
    void testAddFirstUser() {
        Company expectedCompany = new Company("Company 1", "BG123456789", "address", "phone", "email@example.com");
        when(companyService.getCompanyByName("Company 1")).thenReturn(expectedCompany);

        toTest.addFirstUser();
        verify(userRepository).saveAndFlush(userCaptor.capture());
        UserEntity actual = userCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("user01@example.com", actual.getEmail());
        Assertions.assertTrue(passwordEncoder.matches("asdasd", actual.getPassword()));
        Assertions.assertEquals(UserRole.USER, actual.getUserRole());
        Assertions.assertEquals("user01", actual.getFirstName());
        Assertions.assertEquals("user01", actual.getLastName());
        Assertions.assertEquals("Company 1", actual.getCompany().getName());
    }

    @Test
    void testRegisterUser() {
        UserDTO testUserDTO = getUserDTOFromUserEntity(TEST_USER_1);
        testUserDTO.setUserRole(null);

        when(companyService.getCompanyByName(TEST_COMPANY.getName())).thenReturn(TEST_COMPANY);

        toTest.registerUser(testUserDTO);
        verify(userRepository).saveAndFlush(userCaptor.capture());
        UserEntity actual = userCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_USER_1.getEmail(), actual.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(TEST_USER_1.getPassword(), actual.getPassword()));
        Assertions.assertEquals(TEST_USER_1.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(TEST_USER_1.getLastName(), actual.getLastName());
        Assertions.assertEquals(TEST_USER_1.getPhone(), actual.getPhone());
        Assertions.assertEquals(TEST_USER_1.getCompany(), actual.getCompany());
        Assertions.assertEquals(TEST_USER_1.getUserRole(), actual.getUserRole());
    }

    @Test
    void testIsValidUser() {
        ValidateUserDTO testValidateUserDTO = new ValidateUserDTO(TEST_USER_1.getEmail(), TEST_USER_1.getPassword());
        UserEntity encodedTestUser1 = TEST_USER_1;
        encodedTestUser1.setPassword(passwordEncoder.encode(TEST_USER_1.getPassword()));

        when(userRepository.findByEmail(testValidateUserDTO.getEmail())).thenReturn(Optional.of(encodedTestUser1));

        Assertions.assertTrue(toTest.isValidUser(testValidateUserDTO));
    }

    @Test
    void testValidateUser_throwsExceptionWhenPasswordIsIncorrect() {
        ValidateUserDTO testValidateUserDTO = new ValidateUserDTO(TEST_USER_1.getEmail(), TEST_USER_1.getPassword());

        when(userRepository.findByEmail(testValidateUserDTO.getEmail())).thenReturn(Optional.of(TEST_USER_1));

        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.isValidUser(testValidateUserDTO));
    }

    @Test
    void testValidateUser_throwsExceptionWhenUserIsNotFound() {
        ValidateUserDTO testValidateUserDTO = new ValidateUserDTO(TEST_USER_1.getEmail(), TEST_USER_1.getPassword());

        when(userRepository.findByEmail(testValidateUserDTO.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.isValidUser(testValidateUserDTO));
    }

    @Test
    void testGetUserCountByRole() {
        when(userRepository.findAllByUserRole(UserRole.USER)).thenReturn(List.of(TEST_USER_2, TEST_USER_3));

        Assertions.assertEquals(2, toTest.getUserCountByRole(UserRole.USER));
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(TEST_USER_1.getId())).thenReturn(Optional.of(TEST_USER_1));

        UserEntity actual = toTest.getUserById(TEST_USER_1.getId());

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
    void testGetUserById_throwsExceptionWhenUserIsNotFound() {
        when(userRepository.findById(TEST_USER_1.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.getUserById(TEST_USER_1.getId()));
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail(TEST_USER_1.getId())).thenReturn(Optional.of(TEST_USER_1));

        UserEntity actual = toTest.getUserById(TEST_USER_1.getId());

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
    void testGetUserByEmail_throwsExceptionWhenUserIsNotFound() {
        when(userRepository.findByEmail(TEST_USER_1.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.getUserById(TEST_USER_1.getId()));
    }

    @Test
    void testGetCurrentUserData() {
        UserDTO expected = getUserDTOFromUserEntity(TEST_USER_1);
        when(userRepository.findById(TEST_USER_1.getId())).thenReturn(Optional.of(TEST_USER_1));

        UserDTO actual = toTest.getCurrentUserData(TEST_USER_1.getId());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
        Assertions.assertEquals(expected.getPhone(), actual.getPhone());
        Assertions.assertEquals(expected.getUserRole(), actual.getUserRole());
    }

    @Test
    void testGetCurrentUserData_throwsExceptionWhenUserIsNotFound() {
        when(userRepository.findById(TEST_USER_1.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.getCurrentUserData(TEST_USER_1.getId()));
    }

    @Test
    void testEditUserData() {
        EditUserDTO testEditUserDTO = new EditUserDTO("newFirstName", "newLastName", "someCompany", "newPhone");
        when(userRepository.findById(TEST_USER_1.getId())).thenReturn(Optional.of(TEST_USER_1));
        Company expectedCompany = new Company("someCompany", "BG123456789", "address", "phone", "email");
        when(companyService.getCompanyByName(testEditUserDTO.getCompanyName())).thenReturn(expectedCompany);

        toTest.editUserData(testEditUserDTO, TEST_USER_1.getId());
        verify(userRepository).saveAndFlush(userCaptor.capture());
        UserEntity actual = userCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("newFirstName", actual.getFirstName());
        Assertions.assertEquals("newLastName", actual.getLastName());
        Assertions.assertEquals(expectedCompany, actual.getCompany());
        Assertions.assertEquals("newPhone", actual.getPhone());
    }

    @Test
    void testEditUserEmail() {
        UserEmailDTO testUserEmailDTO = new UserEmailDTO("newMail@example.com");
        when(userRepository.findById(TEST_USER_1.getId())).thenReturn(Optional.of(TEST_USER_1));

        toTest.editUserEmail(testUserEmailDTO, TEST_USER_1.getId());
        verify(userRepository).saveAndFlush(userCaptor.capture());
        UserEntity actual = userCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("newMail@example.com", actual.getEmail());
    }

    @Test
    void testEditUserPassword() {
        UserPasswordDTO testUserPasswordDTO = new UserPasswordDTO(TEST_USER_1.getEmail(), TEST_USER_1.getPassword(), "newPassword", "newPassword");
        when(userRepository.findById(TEST_USER_1.getId())).thenReturn(Optional.of(TEST_USER_1));

        toTest.editUserPassword(testUserPasswordDTO, TEST_USER_1.getId());
        verify(userRepository).saveAndFlush(userCaptor.capture());
        UserEntity actual = userCaptor.getValue();

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(passwordEncoder.matches("newPassword", actual.getPassword()));
    }

    @Test
    void testEmptyUserCart() {
        Assertions.assertEquals(1, TEST_USER_1.getCartSize());
        toTest.emptyUserCart(TEST_USER_1);
        Assertions.assertEquals(0, TEST_USER_1.getCartSize());
    }

    private UserDTO getUserDTOFromUserEntity(UserEntity user) {
        return new UserDTO(
                TEST_USER_1.getEmail(),
                TEST_USER_1.getPassword(),
                TEST_USER_1.getPassword(),
                TEST_USER_1.getFirstName(),
                TEST_USER_1.getLastName(),
                TEST_COMPANY.getName(),
                TEST_USER_1.getPhone(),
                UserRole.USER);
    }

}

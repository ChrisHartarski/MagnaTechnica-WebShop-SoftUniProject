package bg.magna.websop.controller;

import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import bg.magna.websop.repository.CompanyRepository;
import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.CompanyService;
import bg.magna.websop.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        companyRepository.deleteAll();

        companyService.addFirstTwoCompanies();
        userService.addAdminUser();
        userService.addFirstUser();
    }

    @Test
    public void testViewRegister() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @Transactional
    public void testRegisterUser() throws Exception {
        UserEntity user = createTestUser("user02@example.com");

        Assertions.assertEquals(2, userRepository.count());

        mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("confirmPassword", user.getPassword())
                        .param("firstName", user.getFirstName())
                        .param("lastName", user.getLastName())
                        .param("companyName", user.getCompany().getName())
                        .param("phone", user.getPhone()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        Assertions.assertEquals(3, userRepository.count());

        UserEntity actual = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(user.getPassword(), actual.getPassword()));
        Assertions.assertEquals(user.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(user.getLastName(), actual.getLastName());
        Assertions.assertEquals(user.getCompany().getName(), actual.getCompany().getName());
        Assertions.assertEquals(user.getPhone(), actual.getPhone());
        Assertions.assertEquals(UserRole.USER, actual.getUserRole());
        Assertions.assertEquals(0, actual.getCartSize());
    }

    @Test
    @Transactional
    public void testRegisterUser_WithAdminUser_CreatesAdminUser() throws Exception {
        UserEntity user = createTestUser("user02@example.com");
        user.setUserRole(UserRole.ADMIN);

        Assertions.assertEquals(2, userRepository.count());

        mockMvc.perform(post("/users/register")
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("confirmPassword", user.getPassword())
                        .param("firstName", user.getFirstName())
                        .param("lastName", user.getLastName())
                        .param("companyName", user.getCompany().getName())
                        .param("phone", user.getPhone())
                        .param("userRole", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        Assertions.assertEquals(3, userRepository.count());

        UserEntity actual = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(user.getPassword(), actual.getPassword()));
        Assertions.assertEquals(user.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(user.getLastName(), actual.getLastName());
        Assertions.assertEquals(user.getCompany().getName(), actual.getCompany().getName());
        Assertions.assertEquals(user.getPhone(), actual.getPhone());
        Assertions.assertEquals(UserRole.ADMIN, actual.getUserRole());
        Assertions.assertEquals(0, actual.getCartSize());
    }

    @Test
    @Transactional
    public void testRegisterUser_returnsIfInputInvalid() throws Exception {
        UserEntity user = createTestUser("user02@example.com");

        Assertions.assertEquals(2, userRepository.count());

        mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .param("email", "")
                        .param("password", user.getPassword())
                        .param("confirmPassword", user.getPassword())
                        .param("firstName", user.getFirstName())
                        .param("lastName", user.getLastName())
                        .param("companyName", user.getCompany().getName())
                        .param("phone", user.getPhone()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));

        Assertions.assertEquals(2, userRepository.count());
        UserEntity actual = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertNull(actual);
    }

    @Test
    @Transactional
    public void testRegisterUser_returnsIfPasswordsNotMatch() throws Exception {
        UserEntity user = createTestUser("user02@example.com");

        Assertions.assertEquals(2, userRepository.count());

        mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("confirmPassword", "someOtherPassword_123")
                        .param("firstName", user.getFirstName())
                        .param("lastName", user.getLastName())
                        .param("companyName", user.getCompany().getName())
                        .param("phone", user.getPhone()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attribute("passwordsDoNotMatch", true));

        Assertions.assertEquals(2, userRepository.count());
        UserEntity actual = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertNull(actual);
    }

    @Test
    @Transactional
    public void testRegisterUser_returnsIfEmailExists() throws Exception {
        UserEntity user = createTestUser("user01@example.com");

        Assertions.assertEquals(2, userRepository.count());

        mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("confirmPassword", user.getPassword())
                        .param("firstName", user.getFirstName())
                        .param("lastName", user.getLastName())
                        .param("companyName", user.getCompany().getName())
                        .param("phone", user.getPhone()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attribute("emailExists", true));

        Assertions.assertEquals(2, userRepository.count());
    }

    @Test
    @Transactional
    public void testRegisterUser_returnsIfCompanyNotExist() throws Exception {
        UserEntity user = createTestUser("user02@example.com");

        Assertions.assertEquals(2, userRepository.count());

        mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("confirmPassword", user.getPassword())
                        .param("firstName", user.getFirstName())
                        .param("lastName", user.getLastName())
                        .param("companyName", "fakeCompany")
                        .param("phone", user.getPhone()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attribute("companyDoesNotExist", true));

        Assertions.assertEquals(2, userRepository.count());
        UserEntity actual = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertNull(actual);
    }

    @Test
    public void testViewLogin() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginUser"));
    }

    @Test
    public void testViewLoginError() throws Exception {
        mockMvc.perform(get("/users/login-error"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginUser"))
                .andExpect(model().attribute("wrongUsernameOrPassword", true));
    }

    @Test
    public void testViewEditUser() throws Exception {
        mockMvc.perform(get("/users/edit")
                        .with(user("user01@example.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user"))
                .andExpect(model().attributeExists("userData"));
    }

    @Test
    public void testEditUser() throws Exception {
        UserEntity user = userRepository.findByEmail("user01@example.com").orElse(null);
        Assertions.assertNotNull(user);

        mockMvc.perform(patch("/users/edit")
                    .with(user("user01@example.com"))
                    .with(csrf())
                    .param("firstName", "newFirstName")
                    .param("lastName", "newLastName")
                    .param("companyName", user.getCompany().getName())
                    .param("phone", "0123456789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        UserEntity actual = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("newFirstName", actual.getFirstName());
        Assertions.assertEquals("newLastName", actual.getLastName());
        Assertions.assertEquals("0123456789", actual.getPhone());
    }

    @Test
    public void testEditUser_returnsIfInputInvalid() throws Exception {
        UserEntity user = userRepository.findByEmail("user01@example.com").orElse(null);
        Assertions.assertNotNull(user);

        mockMvc.perform(patch("/users/edit")
                        .with(user("user01@example.com"))
                        .with(csrf())
                        .param("firstName", "")
                        .param("lastName", "newLastName")
                        .param("companyName", user.getCompany().getName())
                        .param("phone", "0123456789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit"));

        UserEntity actual = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(user.getLastName(), actual.getLastName());
        Assertions.assertEquals(user.getPhone(), actual.getPhone());
    }

    @Test
    public void testEditUser_returnsIfInputCompanyDoesNotExist() throws Exception {
        UserEntity user = userRepository.findByEmail("user01@example.com").orElse(null);
        Assertions.assertNotNull(user);

        mockMvc.perform(patch("/users/edit")
                        .with(user("user01@example.com"))
                        .with(csrf())
                        .param("firstName", "newFirstName")
                        .param("lastName", "newLastName")
                        .param("companyName", "someFakeCompany")
                        .param("phone", "0123456789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit"))
                .andExpect(flash().attribute("companyDoesNotExist", true));

        UserEntity actual = userRepository.findByEmail(user.getEmail()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(user.getLastName(), actual.getLastName());
        Assertions.assertEquals(user.getPhone(), actual.getPhone());
    }

    @Test
    public void testViewEditUserEmail() throws Exception {
        mockMvc.perform(get("/users/edit/email")
                        .with(user("user01@example.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user-email"))
                .andExpect(model().attributeExists("userData"));
    }

    @Test
    public void testEditUserEmail() throws Exception {
        UserEntity user = userRepository.findByEmail("user01@example.com").orElse(null);
        Assertions.assertNotNull(user);
        String userId = user.getId();

        mockMvc.perform(patch("/users/edit/email")
                        .with(user("user01@example.com"))
                        .with(csrf())
                        .param("email", "newEmail@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        UserEntity actual = userRepository.findById(userId).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("newEmail@example.com", actual.getEmail());
    }

    @Test
    public void testEditUserEmail_returnsIfEmailInvalid() throws Exception {
        UserEntity user = userRepository.findByEmail("user01@example.com").orElse(null);
        Assertions.assertNotNull(user);
        String userId = user.getId();

        mockMvc.perform(patch("/users/edit/email")
                        .with(user("user01@example.com"))
                        .with(csrf())
                        .param("email", "someEmail"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit/email"));

        UserEntity actual = userRepository.findById(userId).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    public void testEditUserEmail_returnsIfEmailExists() throws Exception {
        UserEntity user = userRepository.findByEmail("user01@example.com").orElse(null);
        Assertions.assertNotNull(user);
        String userId = user.getId();

        mockMvc.perform(patch("/users/edit/email")
                        .with(user("user01@example.com"))
                        .with(csrf())
                        .param("email", "admin@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit/email"))
                .andExpect(flash().attribute("emailExists", true));

        UserEntity actual = userRepository.findById(userId).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    public void testViewEditUserPassword() throws Exception {
        mockMvc.perform(get("/users/edit/password")
                        .with(user("user01@example.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user-password"))
                .andExpect(model().attributeExists("userPasswordData"));
    }

    @Test
    public void testEditUserPassword() throws Exception {
        UserEntity newUser = createTestUserAndSaveToDB("user02@example.com");
        Assertions.assertTrue(userRepository.findByEmail("user02@example.com").isPresent());
        Assertions.assertTrue(passwordEncoder.matches("Password_123", newUser.getPassword()));

        mockMvc.perform(patch("/users/edit/password")
                        .with(user(newUser.getEmail()).roles("USER"))
                        .with(csrf())
                        .param("currentPassword", "Password_123")
                        .param("password", "newPassword_123")
                        .param("confirmPassword", "newPassword_123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        UserEntity actual = userRepository.findByEmail(newUser.getEmail()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(passwordEncoder.matches("newPassword_123", actual.getPassword()));
    }

    @Test
    public void testEditUserPassword_returnsIfInputIsInvalid() throws Exception {
        UserEntity newUser = createTestUserAndSaveToDB("user02@example.com");
        Assertions.assertTrue(userRepository.findByEmail("user02@example.com").isPresent());
        Assertions.assertTrue(passwordEncoder.matches("Password_123", newUser.getPassword()));

        mockMvc.perform(patch("/users/edit/password")
                        .with(user(newUser.getEmail()).roles("USER"))
                        .with(csrf())
                        .param("currentPassword", "Password_123")
                        .param("password", "")
                        .param("confirmPassword", "newPassword_123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit/password"));

        UserEntity actual = userRepository.findByEmail(newUser.getEmail()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertFalse(passwordEncoder.matches("newPassword_123", actual.getPassword()));
    }

    @Test
    public void testEditUserPassword_returnsIfCurrentPasswordIsIncorrect() throws Exception {
        UserEntity newUser = createTestUserAndSaveToDB("user02@example.com");
        Assertions.assertTrue(userRepository.findByEmail("user02@example.com").isPresent());
        Assertions.assertTrue(passwordEncoder.matches("Password_123", newUser.getPassword()));

        mockMvc.perform(patch("/users/edit/password")
                        .with(user(newUser.getEmail()).roles("USER"))
                        .with(csrf())
                        .param("currentPassword", "fakePassword_123")
                        .param("password", "newPassword_123")
                        .param("confirmPassword", "newPassword_123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit/password"))
                .andExpect(flash().attribute("currentPasswordIncorrect", true));

        UserEntity actual = userRepository.findByEmail(newUser.getEmail()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(newUser.getPassword(), actual.getPassword());
    }

    @Test
    public void testEditUserPassword_returnsIfPasswordsDoNotMatch() throws Exception {
        UserEntity newUser = createTestUserAndSaveToDB("user02@example.com");
        Assertions.assertTrue(userRepository.findByEmail("user02@example.com").isPresent());
        Assertions.assertTrue(passwordEncoder.matches("Password_123", newUser.getPassword()));

        mockMvc.perform(patch("/users/edit/password")
                        .with(user(newUser.getEmail()).roles("USER"))
                        .with(csrf())
                        .param("currentPassword", "Password_123")
                        .param("password", "newPassword_123")
                        .param("confirmPassword", "newPassword_1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit/password"))
                .andExpect(flash().attribute("passwordsDoNotMatch", true));

        UserEntity actual = userRepository.findByEmail(newUser.getEmail()).orElse(null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(newUser.getPassword(), actual.getPassword());
    }


    private UserEntity createTestUser(String email){
        return new UserEntity(
                "someUUID",
                email,
                "Password_123",
                "Test",
                "User",
                "0888888888",
                UserRole.USER,
                new HashMap<>(),
                new ArrayList<>(),
                companyRepository.findByName("Company 1").get());
    }

    private UserEntity createTestUserAndSaveToDB(String email){
        UserEntity user = createTestUser(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }
}

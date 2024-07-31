package bg.magna.websop.config;

import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.impl.CurrentUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                //grant access to static resources
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                //grant access to pages
                                .requestMatchers("/", "/machines/all", "/machines/{id}", "/parts/all","/parts/{partCode}", "/contact", "/users/login", "/users/login-error", "/users/register", "/companies/add").permitAll()
                                //grant access only to admin users
                                .requestMatchers("/admin-panel", "/machines/add", "/machines/delete/", "/machines/edit/{id}", "/brands/add", "/admin-panel/initializeMockDB", "/orders/dispatch/{id}", "/orders/deliver/{id}", "/parts/add", "/parts/edit/{partCode}", "/parts/delete/{partCode}", "/machines/enquiries/all", "/machines/enquiries/delete/{id}").hasRole("ADMIN")
                                //grant access only to normal users
                                .requestMatchers("/cart", "/cart/remove-item/", "/orders/add", "machines/enquiries/all", "/machines/enquiries/{id}").hasRole("USER")
                                .anyRequest()
                                .authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/users/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/users/login-error")
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/users/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true))
                .build();
    }

    @Bean
    public CurrentUserDetailsService userDetailsService(UserRepository userRepository) {
        return new CurrentUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

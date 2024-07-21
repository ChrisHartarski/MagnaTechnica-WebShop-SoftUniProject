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
                                .requestMatchers("/", "/machines", "machines/{id}", "/spare-parts","/parts/{partCode}", "/company/", "/contact", "/users/login", "/users/login-error", "/users/register", "/company/add").permitAll()
                                //grant access only to admin users
                                .requestMatchers("/admin-panel", "/machines/add", "/machines/delete/", "/machines/edit/", "/brands/add", "/admin-panel/initializeMockDB", "/orders/all", "/orders/dispatch/", "/orders/deliver/", "/parts/add", "/parts/edit/", "/parts/delete/", "/machines/enquiries/all").hasRole("ADMIN")
                                //grant access only to normal users
                                .requestMatchers("/cart", "/cart/remove-item/", "/orders/add", "machines/enquiries/").hasRole("USER")
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

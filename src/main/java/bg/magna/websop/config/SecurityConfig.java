package bg.magna.websop.config;

import bg.magna.websop.repository.UserRepository;
import bg.magna.websop.service.impl.MagnaUserDetailsService;
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
                                .requestMatchers("/", "/web-shop", "/company/", "/contact", "/users/login", "users/register").permitAll()
                                //grant access only to admin users
                                .requestMatchers("/admin-panel", "/brands/add", "/admin-panel/initializeMockDB", "/orders/dispatch/", "/orders/deliver/", "/parts/add", "/parts/edit/", "/parts/delete/").hasRole("ADMIN")
                                //grant access only to normal users
                                .requestMatchers("/cart", "/cart/remove-item/", "/orders/add").hasRole("USER")
                                .anyRequest()
                                .authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/users/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/web-shop")
                                .failureForwardUrl("/users/login-error"))
                .logout(logout ->
                        logout
                                .logoutUrl("/users/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true))
                .build();
    }

    @Bean
    public MagnaUserDetailsService userDetailsService(UserRepository userRepository) {
        return new MagnaUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
package com.united.config;

import com.united.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm -> httpForm
                        .loginPage("/login").permitAll()
                        .failureHandler((request, response, exception) -> {
                            request.getSession().setAttribute("error", "Invalid username or password!");
                            response.sendRedirect("/login");
                        })
                        .successHandler((request, response, authentication) -> {
                            boolean hasRole = authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().startsWith("ROLE_"));

                            String redirectUrl = hasRole
                                    ? "/home"
                                    : "/profile/" + authentication.getName();

                            new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication != null) {
                                String username = authentication.getName();
                                userService.updateLastActiveDate(username);
                            }
                            response.sendRedirect("/login?logout");
                        })
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/super-admin/**").hasRole("SUPER_ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/home"))
                )
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}





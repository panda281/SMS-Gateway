package com.meinab.smsgateway.configs;

import com.meinab.smsgateway.enums.Authority;
import com.meinab.smsgateway.models.Users;
import com.meinab.smsgateway.services.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {
    private final UsersService usersService;
    private static final String ADMIN = "admin";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize->authorize.anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usersService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        createAdminUser();
        return authProvider;
    }
    public void createAdminUser() {
        try {
            usersService.loadUserByUsername(ADMIN);
            log.info("admin found");
        } catch (Exception e) {
            Users users = Users.builder()
                    .userName(ADMIN)
                    .password(passwordEncoder().encode(ADMIN))
                    .authority(Authority.ADMIN)
                    .isActive(true).build();
            usersService.createUpdateUser(users);
            log.info("admin created");
        }
    }


}

package org.example.adds.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class Security {
    private final CustomUserDetailsService customUserDetailsService;

    private final String[] SWAGGER_URLS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Filter filter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http
                .authorizeHttpRequests((auth) -> auth
                                .requestMatchers(SWAGGER_URLS).permitAll()
                                .requestMatchers(
                                        "/api/auth/delete/**", // delete drafted user for test
                                        "/api/user/delete/**", // delete actual user for test
                                        "/admin-chat.html",
                                        "/actuator/**",
                                        // this has to be private for authenticated users
                                        "/websocket-connection/**",
                                        "/notification",
                                        "/favicon.ico",
                                        "/api/auth/signup",
                                        "/api/auth/login",
                                        "/api/auth/verify",
                                        "/api/auth/forgot/verify",
                                        "/api/auth/forgot",
                                        "/api/auth/reset",
                                        "/api/auth/resend-sms",
                                        "/api/adv/get/**",
                                        "/api/chat/send-to-admin" // this endpoints have to be authenticated
                                ).permitAll()
                                .requestMatchers("/api/adv/create-link/**",
                                        "/api/adv/qr-code/**",
                                        "/api/adv/get-by/**",
                                        "/api/adv/delete",
                                        "/api/adv/delete-view",
                                        "/api/adv/update-status",
                                        "/api/adv/qr-code/download/**").hasRole("USER") // only users
                                .requestMatchers("/api/chat/send-to-user",
                                        "/api/chat/view-one/**",
                                        "/api/chat/get-all-chat",
                                        "/api/wallet/deposit",
                                        "/api/admin/getUser/transactions",
                                        "/api/admin/getUser").hasRole("ADMIN") // only admin
                                .requestMatchers("/api/transaction/get/**",
                                        "/wss/**",
                                        "/ws/**",
                                        "/chat/**").hasAnyRole("USER", "ADMIN")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.userDetailsService(customUserDetailsService);
        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

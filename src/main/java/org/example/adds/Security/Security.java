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
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_URLS).permitAll()
                        .requestMatchers(
                                "/favicon.ico",
                                /* channel should be open
                                and websocket itself check a user authorized or not
                                while handshaking at first
                                */
                                "/chat/**",
                                "/ws/**",
                                "/wss/**",
                                "/api/auth/delete/**",
                                "/api/user/delete/**",
                                "/user-chat.html",
                                "/actuator/**",
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/auth/verify",
                                "/api/auth/forgot/verify",
                                "/api/auth/forgot",
                                "/api/auth/reset",
                                "/api/auth/resend-sms",
                                "/api/adv/get/**",
                                "/api/chat/send-to-admin"
                        ).permitAll()
                        .requestMatchers(
                                "/api/adv/create-link/**",
                                "/api/adv/get-by/**",
                                "/api/adv/delete",
                                "/api/adv/delete-view",
                                "/api/adv/update-status",
                                "/api/qrcode/create/**",
                                "/api/qrcode/download/**"
                        ).hasRole("USER")
                        .requestMatchers(
                                "/api/chat/send-to-user",
                                "/api/chat/view-one/**",
                                "/api/chat/get-all-chat",
                                "/api/wallet/deposit",
                                "/api/admin/getUser/transactions",
                                "/api/admin/getUser",
                                "/api/admin/get-all/adv"
                        ).hasRole("ADMIN")
                        .requestMatchers("/api/transaction/get/**").hasAnyRole("USER", "ADMIN")
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

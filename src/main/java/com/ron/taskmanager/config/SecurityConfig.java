package com.ron.taskmanager.config;

import com.ron.taskmanager.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder BCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager AuthenticationManager(AuthenticationConfiguration configuration){
        try {
            return configuration.getAuthenticationManager();
        }
        catch (Exception e){
            throw new RuntimeException("Failed to create AuthenticationManager: " + e.getMessage(), e);
        }
    }

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/api/auth/**","/swagger-ui/**","/v3/api-docs/**")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter ,UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}

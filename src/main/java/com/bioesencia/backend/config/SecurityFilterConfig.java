package com.bioesencia.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityFilterConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println(">>> Custom SecurityFilterConfig loaded!");
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .anyRequest().permitAll()
            .and()
            .formLogin().disable()
            .httpBasic().disable();

        return http.build();
    }

}

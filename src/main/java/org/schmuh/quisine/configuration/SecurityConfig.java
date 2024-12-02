package org.schmuh.quisine.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and() // Enable CORS support
                .csrf().disable() // Optional: Disable CSRF for development/testing
                .authorizeHttpRequests()
                .anyRequest().permitAll(); // Allow all requests for testing
        return http.build();
    }
}
package org.schmuh.quisine.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration Class for SecurityConfig.
 * This disables cors and csrf for development phase
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain for the application.
     *
     * <p>This method sets up the {@link SecurityFilterChain} with the following configurations:
     * <ul>
     *     <li>Enables Cross-Origin Resource Sharing (CORS) to allow requests from different origins.</li>
     *     <li>Disables Cross-Site Request Forgery (CSRF) protection, typically for development or testing purposes.</li>
     *     <li>Permits all incoming requests without authentication or authorization, primarily for testing environments.</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} object used to configure the security settings
     * @return a configured {@link SecurityFilterChain} instance
     * @throws Exception if an error occurs during configuration
     */
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
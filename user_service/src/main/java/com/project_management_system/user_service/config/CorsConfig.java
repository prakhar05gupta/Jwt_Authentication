package com.project_management_system.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        //Allow specific origins (change for production)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",   // React default
                "http://localhost:4200",   // Angular default
                "http://localhost:8080",   // API Gateway
                "http://localhost:5500",   // VS Code Live Server
                "http://127.0.0.1:5500",    // VS Code Live Server alternate
            "https://prakhar05gupta.github.io"
        ));
        //Allow specific HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));
        //Allow specific headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        //Allow credentials
        configuration.setAllowCredentials(true);

        //Expose headers
        configuration.setExposedHeaders(List.of("Authorization"));

        // Max age
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);

        return source ;
    }
}

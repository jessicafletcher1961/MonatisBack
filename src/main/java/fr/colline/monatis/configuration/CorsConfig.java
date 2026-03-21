package fr.colline.monatis.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Appliquer à toutes les routes
                        .allowedOrigins("http://localhost:5173") // Autoriser VITE
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes permises
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
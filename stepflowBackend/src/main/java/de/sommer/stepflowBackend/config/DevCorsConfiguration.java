// package de.sommer.stepflowBackend.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;
// import org.springframework.lang.NonNull;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// @Profile("development")
// public class DevCorsConfiguration implements WebMvcConfigurer {
//     @Override
//     public void addCorsMappings(@NonNull CorsRegistry registry) {
//         registry.addMapping("/api/**")
//                 .allowedOriginPatterns("*")
//                 .allowedHeaders("*")
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                 .allowCredentials(true);
//     }
// }
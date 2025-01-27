package org.example.adds.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc // added
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("/**", "https://sculpin-golden-bluejay.ngrok-free.app", "http://localhost:9091")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(false)
//                .maxAge(3600)
//                .allowedHeaders("Accept", "Content-Type", "Origin",
//                        "Authorization", "X-Auth-Token")
//                .exposedHeaders("X-Auth-Token", "Authorization")
//                .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS");
//    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**","/webjars/**")
                .addResourceLocations("classpath:/static/", "classpath:/META-INF/resources/webjars/");
    }
}

//package org.example.adds.WebSocket;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns(
//                        "http://localhost:9000",
//                        "https://sculpin-golden-bluejay.ngrok-free.app",
//                        "http://localhost:3000",
//                        "http://127.0.0.1:5500",
//                        "http://172.16.15.112:3000")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowCredentials(true);
//    }
//
//
////    @Override
////    public void addResourceHandlers(ResourceHandlerRegistry registry) {
////        registry.addResourceHandler("/**", "/webjars/**")
////                .addResourceLocations("classpath:/static/", "classpath:/META-INF/resources/webjars/");
////    }
//}

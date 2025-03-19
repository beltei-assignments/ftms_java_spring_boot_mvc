package com.example.ftms_java_spring_boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.ftms_java_spring_boot.controllers.Security;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Security())
                .addPathPatterns("/**") // Apply to all routes
                .excludePathPatterns("/register", "/css/**", "/js/**", "/images/**", "/webjars/**");
    }
}

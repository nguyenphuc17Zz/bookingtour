package com.example.bookingtour.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/description_img/**")
                .addResourceLocations("file:src/main/resources/static/description_img/");
        registry.addResourceHandler("/thumbnails_img/**")
                .addResourceLocations("file:src/main/resources/static/thumbnails_img/");
    }
}

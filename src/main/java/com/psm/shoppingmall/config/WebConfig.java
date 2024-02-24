package com.psm.shoppingmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 프론트 엔드는 3000번 포트인데, 백엔드 포트는 8080이다.
    // 이 설정을 3030->8080 api를 호출할 수 있도록 설정해줘야 한다.

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PATCH", "PUT", "OPTIONS", "DELETE")
            .allowCredentials(true);
    }
}

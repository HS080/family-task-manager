package com.example.demo; // 包名必须和你的启动类一致！你这里是com.example.demo，完全正确

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 必须加，SpringBoot才会识别这是配置类
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有接口跨域
                .allowedOriginPatterns("*") // 允许所有前端地址
                .allowedMethods("*") // 允许所有请求方式（GET/POST等）
                .allowCredentials(true); // 允许携带cookie（登录用）
    }
}
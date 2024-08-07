package com.ssafy.a505.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.a505.global.interceptor.JwtInterceptor;

//Web 관련 설정
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //자원 설정
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    // CORS 에러 전역 처리
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("POST", "GET", "PUT", "DELETE");
    }

	// 인터셉터 등 처리 가능
	@Autowired
	private JwtInterceptor jwtInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor((HandlerInterceptor) jwtInterceptor).addPathPatterns("/**")
		.excludePathPatterns("/api-user/**", "/swagger-ui/**", "/v3/api-docs/**","/**");
	}

}




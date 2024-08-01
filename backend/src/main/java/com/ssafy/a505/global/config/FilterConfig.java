
package com.ssafy.a505.global.config;

import com.ssafy.a505.filter.CustomCorsFilter;
import com.ssafy.a505.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CustomCorsFilter customCorsFilter;

    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtFilter() {
        FilterRegistrationBean<JwtRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtRequestFilter);
        registrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴 설정
        registrationBean.setOrder(1); // Order 설정
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CustomCorsFilter> corsFilter() {
        FilterRegistrationBean<CustomCorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(customCorsFilter);
        registrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴 설정
        registrationBean.setOrder(0); // 우선순위를 가장 높게 설정
        return registrationBean;
    }
}

package com.company.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecuredFilterConfig {
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public FilterRegistrationBean<JwtFilter> filterRegistrationBean(){
        FilterRegistrationBean<JwtFilter>  bean = new FilterRegistrationBean<JwtFilter>();
        bean.setFilter(jwtFilter);

        bean.addUrlPatterns("/profile/*");
        bean.addUrlPatterns("/types/adm/*");
        bean.addUrlPatterns("/region/adm/*");
        bean.addUrlPatterns("/article_like/*");
        bean.addUrlPatterns("/article/adm/*");
        bean.addUrlPatterns("/category/*");
        bean.addUrlPatterns("/sms/*");
        return bean;
    }
}

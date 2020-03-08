package com.ofs.web.config;

import com.ofs.web.handler.AuthHandlerInterceptor;
import com.ofs.web.resolver.JwtTokenArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author gaoly
 * @version 2019/9/13
 */
@Configuration
@Slf4j
public class SpringMvcConfig implements WebMvcConfigurer {


    /**
     * 登陆验证拦截器
     *
     * @return
     */
    public AuthHandlerInterceptor getAuthHandlerInterceptor() {
        log.info("----------------创建用户验证拦截器");
        return new AuthHandlerInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("----------------注册拦截器拦截所有/**");
//        registry.addInterceptor(getAuthHandlerInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/login", "/swagger-ui.html", "/**/*.js", "/**/*.css", "/swagger-resources/**", "/**/*.png");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new JwtTokenArgumentResolver());
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
        // 放行swagger
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}

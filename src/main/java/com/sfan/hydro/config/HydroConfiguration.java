package com.sfan.hydro.config;

import com.sfan.hydro.support.*;
import com.sfan.hydro.util.pagination.SqlDialect;
import com.sfan.hydro.util.pagination.dialects.MysqlDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HydroConfiguration implements WebMvcConfigurer {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private ThemeInterceptor themeInterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private PjaxInterceptor pjaxInterceptor;
    @Autowired
    private InterviewInterceptor interviewInterceptor;
    @Autowired
    private BlockInitialInterceptor initialInterceptor;
    @Autowired
    private CustomLocaleResolver localeResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/image/**")
                .excludePathPatterns("/**/*.js")
                .excludePathPatterns("/**/*.css");

        registry.addInterceptor(interviewInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error/**")
                .excludePathPatterns("/initial/**")
                .excludePathPatterns("/**/*.*");

        registry.addInterceptor(pjaxInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/image/**")
                .excludePathPatterns("/**/*.js")
                .excludePathPatterns("/**/*.css");

        registry.addInterceptor(initialInterceptor)
                .addPathPatterns("/initial/**")
                .excludePathPatterns("/**/*.*");

        registry.addInterceptor(themeInterceptor)
                .excludePathPatterns("/**/*.js")
                .excludePathPatterns("/**/*.css")
                .excludePathPatterns("/initial/**")
                .excludePathPatterns("/media/**")
                .excludePathPatterns("/themes/**")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/error/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/admin/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/themes/**")
                .addResourceLocations("classpath:/templates/themes/");

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/templates/themes/");
    }

    @Bean
    public SqlDialect sqlDialect(){
        SqlDialect sqlDialect = new MysqlDialect();
        return sqlDialect;
    }

    @Bean
    public LocaleResolver localeResolver(){
        return localeResolver;
    }
}

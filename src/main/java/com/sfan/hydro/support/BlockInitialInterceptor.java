package com.sfan.hydro.support;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class BlockInitialInterceptor implements HandlerInterceptor {

    private Properties properties = new Properties();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Resource resource = new ClassPathResource("configuration/webSiteConfig.properties");
        try (InputStream inputStream = resource.getInputStream()){
            properties.load(inputStream);
        } catch (IOException e){

        }
        if(!Boolean.valueOf(properties.getProperty("FirstSetUp.account"))){
            response.sendRedirect("/");
            return false;
        }else{
            return true;
        }
    }
}

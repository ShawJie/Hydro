package com.sfan.hydro.support;

import com.sfan.hydro.domain.expand.Theme;
import com.sfan.hydro.service.ThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@WebFilter(filterName = "staticResourceFilter", urlPatterns = "/*")
public class StaticResourceFilter implements Filter {

    private List<String> excludePathPatterns;
    private PathMatcher pathMatcher;
    private List<String> includeFileType;

    private Properties properties;

    private Logger logger = LoggerFactory.getLogger(StaticResourceFilter.class);

    @Autowired
    private ThemeService themeService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        boolean isForward = false;

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String referer = request.getHeader("referer");

        if(excludePathPatterns.stream().filter(pattern -> pathMatcher.match(pattern.trim(), request.getRequestURI())).count() > 0){
            referer = null;
        }

        if(referer != null){
            URL url = new URL((referer.endsWith("/") ? referer.substring(0, referer.length() - 1) : referer ) + request.getRequestURI());
            boolean isTouristPage = excludePathPatterns.stream().filter(pattern -> pathMatcher.match(pattern.trim(), url.getPath())).count() == 0;
            if(isTouristPage){
                Theme theme = themeService.getCurrentThemeConfig();
                // this request is send by main page
                final String uri = request.getRequestURI();
                boolean isLimitType = includeFileType.stream().filter(type -> uri.endsWith(type.trim())).count() > 0;
                if(isLimitType) {
                    StringBuilder path = new StringBuilder(theme.getThemePath());
                    path.append(uri.startsWith("/") ? uri.substring(1) : uri);
                    isForward = true;
                    request.getRequestDispatcher("/" + path.toString()).forward(servletRequest, servletResponse);
                }
            }
        }
        if(!isForward){
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        properties = new Properties();
        try {
            properties.load(new ClassPathResource("configuration/webSiteConfig.properties").getInputStream());
        }catch (IOException e){
            logger.error("cannot load the config in webSiteConfig.properties");
            throw new RuntimeException("initial StaticResourceFilter failed");
        }

        String[] excludePath = properties.getProperty("StaticResourceFilter.excludePathPatterns").split(",");
        String[] includeType = properties.getProperty("StaticResourceFilter.includeFileType").split(",");

        excludePathPatterns = new ArrayList<>(Arrays.asList(excludePath));

        includeFileType = new ArrayList<>(Arrays.asList(includeType));

        pathMatcher = new AntPathMatcher();
    }
}

package com.sfan.hydro.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfan.hydro.attach.HydroNotFoundException;
import com.sfan.hydro.domain.enumerate.SystemConst;
import com.sfan.hydro.domain.expand.Theme;
import com.sfan.hydro.service.ThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class ThemeInterceptor implements HandlerInterceptor {

    private final String THEME_FOLDER = "themes/";

    @Autowired
    private ThemeService themeService;

    private Logger logger = LoggerFactory.getLogger(ThemeInterceptor.class);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(response.getHeader("Content-Type") == null) {
            if (modelAndView != null && !modelAndView.isEmpty() && modelAndView.hasView()) {
                if(modelAndView.getModel().get("routeState") != SystemConst.Straight){
                    Theme theme = themeService.getCurrentThemeConfig();

                    StringBuilder themeRealPath = new StringBuilder(THEME_FOLDER);
                    themeRealPath.append(theme.getThemePath());
                    modelAndView.setViewName(themeRealPath.append(modelAndView.getViewName()).toString());

                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, String> options = new HashMap<>();
                    modelAndView.addObject("option", options);
                    theme.getOption().forEach((k, v) -> {
                        JsonNode node = null;
                        try {
                            node = mapper.readTree(v);
                        }catch (Exception e){
                            logger.error("cannot parse to json: %s", v);
                            throw new RuntimeException();
                        }
                        options.put(k, node.path("value").textValue());
                    });
                }
            }
        }
    }
}

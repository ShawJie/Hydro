package com.sfan.hydro.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfan.hydro.domain.enumerate.SystemConst;
import com.sfan.hydro.domain.expand.Theme;
import com.sfan.hydro.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ThemeInterceptor implements HandlerInterceptor {

    private final String THEME_FOLDER = "themes/";

    @Autowired
    private ThemeService themeService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(response.getHeader("Content-Type") == null) {
            if (!modelAndView.isEmpty() && modelAndView.hasView()) {
                if(modelAndView.getModel().get("routeState") != SystemConst.Straight){
                    Theme theme = themeService.getCurrentThemeConfig();

                    StringBuilder themeRealPath = new StringBuilder(THEME_FOLDER);
                    themeRealPath.append(theme.getThemePath());
                    modelAndView.setViewName(themeRealPath.append(modelAndView.getViewName()).toString());

                    ObjectMapper mapper = new ObjectMapper();
                    theme.getOption().forEach((k, v) -> {
                        JsonNode node = null;
                        try {
                            node = mapper.readTree(v);
                        }catch (Exception e){
                            throw new RuntimeException(e);
                        }
                        modelAndView.addObject(k, node.path("value").textValue());
                    });
                }
            }
        }
    }
}

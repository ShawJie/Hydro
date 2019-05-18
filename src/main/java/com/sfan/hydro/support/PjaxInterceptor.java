package com.sfan.hydro.support;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PjaxInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String xPjax = request.getHeader("x-pjax");
        if(xPjax != null && Boolean.valueOf(xPjax)){
            modelAndView.getModel().put("pjaxLoad", true);
        }
    }
}

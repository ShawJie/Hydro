package com.sfan.hydro.support;

import com.sfan.hydro.domain.enumerate.SystemConst;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object author = session.getAttribute(SystemConst.User.getVal());
        if(author != null){
            return true;
        }
        response.sendRedirect("/admin/login");
        return false;
    }
}



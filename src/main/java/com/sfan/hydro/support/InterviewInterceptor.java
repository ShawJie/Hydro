package com.sfan.hydro.support;

import com.sfan.hydro.domain.model.VisitInfo;
import com.sfan.hydro.service.VisitInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Component
public class InterviewInterceptor implements HandlerInterceptor {

    private static Set<String> ipSet = new HashSet<>();
    private static Timer timer = new Timer();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VisitInfoService visitInfoService;

    @SuppressWarnings("all")
    @PostConstruct
    private void initial(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ipSet.clear();
                logger.info(">>> Interview ip cache has been clear <<<");
            }
        };

        Calendar now = Calendar.getInstance();
        now.set(Calendar.DATE, now.get(Calendar.DATE) + 1);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);

        timer.schedule(timerTask, now.getTime(), (1000 * 60 * 60 * 24));
        logger.info(">>> Interview ip cache timer start <<<");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        if (ipSet.contains(ip)){
            return true;
        }

        VisitInfo info = new VisitInfo(ip, request.getRequestURI(), Calendar.getInstance().getTime());
        visitInfoService.saveVisitInfo(info);
        ipSet.add(ip);
        return true;
    }
}

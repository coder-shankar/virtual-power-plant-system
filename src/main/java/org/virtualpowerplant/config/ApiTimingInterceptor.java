package org.virtualpowerplant.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiTimingInterceptor implements HandlerInterceptor {
    private static final String START_TIME = "startTime";
    private static final Logger logger = LoggerFactory.getLogger(ApiTimingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        var startTime = (Long) request.getAttribute(START_TIME);
        if (startTime != null) {
            var duration = System.currentTimeMillis() - startTime;
            var method = request.getMethod();
            var uri = request.getRequestURI();
            logger.info("{} {} took {} ms", method, uri, duration);
        }
    }
}
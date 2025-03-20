package com.example.ftms_java_spring_boot.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class Security implements HandlerInterceptor {
    private final String[] unprotectedRoutes = { "/login", "/forgot_password", "/verify_code" };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();

        boolean isMeetUnProtectedRoute = Arrays.asList(unprotectedRoutes).contains(requestURI);

        // Allow to login when there is no session userId and current route is /login
        if (isMeetUnProtectedRoute && (session == null || session.getAttribute("userId") == null)) {
            return true;
        }

        // Redirect to login when try protected routes without session userId
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("/login");
            return false;
        }

        // If the user is already logged in, prevent access to login
        if (isMeetUnProtectedRoute) {
            // Redirect to default page
            response.sendRedirect("/");
            return false;
        }

        // Everything went fine
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
        // Optional: Add extra logic after the request is handled
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        // Cleanup after request completion
    }
}

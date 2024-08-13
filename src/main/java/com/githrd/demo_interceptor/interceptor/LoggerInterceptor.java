package com.githrd.demo_interceptor.interceptor;

import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //로그인정보 얻어오기
        Map<String,Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
        //로그인이 안되어있으면...(세션에 유저가 없으면...)
        if(user==null){
            response.sendRedirect("../err?reason=session_timeout");
            return false; // 진입을 막아야한다.
        }

        String uri = request.getRequestURI();
        // uri = /admin/members
        // uri = /adult/...

        if (uri.startsWith("/admin/")) {
           // 관리자 페이지냐? 
           String grade = (String)user.get("grade");
           // 등급이 관리자가 아니면...
           if(!grade.equals("admin")){
                response.sendRedirect("../err?reason=not_admin");
                return false; // 진입을 막아야한다.
           }
        }else if (uri.startsWith("/adult/")) {
            // 성인만 이용가능한 경로
            int age = Integer.parseInt((String) user.get("age"));
            if(age<20){
                response.sendRedirect("../err?reason=not_adult");
                return false; // 진입을 막아야한다.
            }
            
        }


        //log.info("==================== BEGIN ====================");
        //log.info("Request URI ===> " + request.getRequestURI());
        System.out.println("==================== BEGIN ====================");
        System.out.println("Request URI ===> " + request.getRequestURI());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //log.info("==================== END ======================");
        System.out.println("==================== END ======================");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

}

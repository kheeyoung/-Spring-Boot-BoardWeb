package com.example.MyHomePage;

import com.example.MyHomePage.Memeber.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor { //로그인이 필요한 페이지 접근시 인터샙터 통해 로그인 여부 확인
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if(session!=null){
            MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기
            if(loginedMemberDTO!=null){
                return true;
            }
        }
        response.sendRedirect("/login/loginForm");
        return false;
    }


}


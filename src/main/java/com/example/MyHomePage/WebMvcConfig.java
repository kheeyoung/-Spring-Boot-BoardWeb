package com.example.MyHomePage;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) { //로그인이 필요한 페이지 접근시 인터샙터 통해 로그인 여부 확인-> 없을 시 로그인 화면으로 가는 페이지들 설정
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/board/boardToUpdatePost");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/board/boardToDeletePost");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/board/boardToPost");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/gift");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/manager");
    }

}

package com.example.MyHomePage.Memeber;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class MemberController {
    @Autowired
    MemberService MemberService;
    //매핑
    //로그인 창
    @GetMapping("/login/loginForm")
    public String login(){
        String nextPage = "login/loginForm";
        return nextPage;
    }
    //회원가입창
    @GetMapping("/login/login_signIn")
    public String login_signIn(){
        String nextPage = "login/login_signIn";
        return nextPage;
    }



    //기능
    //로그인
    @PostMapping("/login/loginConfirm")
    public String loginConfirm(MemberDTO memberDTO, HttpSession session, Model model){
        log.info("[memberController] loginConfirm");
        model.addAttribute("result","성공");
        model.addAttribute("reason","로그인 성공");

        String nextPage="/result";

        MemberDTO loginedMemberDTO=MemberService.loginConfirm(memberDTO); //서비스로부터 로그인 정보를 받아온다.
        if(loginedMemberDTO == null){
            //만약 로그인 실패시 실패 페이지를 보여준다.
            model.addAttribute("result","실패");
            model.addAttribute("reason","로그인 실패. 아이디와 패스워드를 확인해주세요.");

        }
        else{
            session.setAttribute("loginedMemberDTO",loginedMemberDTO);
            session.setMaxInactiveInterval(60*30);
        }
        return nextPage;
    }

    //회원가입
    @PostMapping("/login/login_signIn_Confirm")
    public String login_signIn_Confirm(MemberDTO memberDTO, @RequestParam(value="m_mail1") String m_mail1,
                                                            @RequestParam(value="m_mail2") String m_mail2,
                                                            @RequestParam(value="m_mail3") String m_mail3, Model model){
        log.info("[memberController] login_signIn");
        model.addAttribute("result","성공");
        model.addAttribute("reason","회원가입 성공");
        String nextPage="/result";
        //메일 설정
        if(m_mail2.equals("직접 입력")){
            memberDTO.setM_mail(m_mail1 + "@" + m_mail3);
        }
        else {
            memberDTO.setM_mail(m_mail1 + "@" + m_mail2);
        }
        int result=MemberService.signIn(memberDTO); //서비스로부터 로그인 정보를 받아온다. = 기존에 등록된 같은 사용자가 있는지 확인.
        if(result<=0){
            model.addAttribute("result","실패");
            model.addAttribute("reason","회원가입 실패. 같은 아이디의 사용자가 있습니다.");
        }

        return nextPage;
    }

    //맴버 수정용 (관리자)
    @PostMapping("/manager/editMember")
    public String editMember(MemberDTO memberDTO){
        log.info("[memberController] editMember");
        String nextPage="redirect:/manager";

        MemberService.editMember(memberDTO); //맴버 정보 수정된 거 업데이트

        return nextPage;
    }





    }

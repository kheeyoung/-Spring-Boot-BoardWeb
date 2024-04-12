package com.example.MyHomePage.controller;

import com.example.MyHomePage.Memeber.MemberDTO;
import com.example.MyHomePage.Memeber.MemberService;
import com.example.MyHomePage.gift.GiftDTO;
import com.example.MyHomePage.gift.GiftService;
import com.example.MyHomePage.myPage.MyPageService;
import com.example.MyHomePage.myPage.SpecialGiftDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class mainController {
    @Autowired
    MemberService MemberService;
    @Autowired
    GiftService GiftService;
    @Autowired
    MyPageService MyPageService;

    //페이지 매핑

    //메인페이지
    @GetMapping("/main")
    public String main(Model model, HttpSession session){
        System.out.println("[mainController] main");
        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기
        if(loginedMemberDTO==null) { //로그인 정보가 없으면
            model.addAttribute("username", "로그인을 해주세요!");
        }
        else{
            model.addAttribute("username", loginedMemberDTO.getM_id()+" 님 어서오세요!");
        }
        String nextPage="main";
        return nextPage;
    }

    //로그아웃 기능
    @GetMapping("/logout")
    public String logout(HttpSession session){
        System.out.println("[mainController] logout");
        String nextPage="redirect:/main";
        session.invalidate();
        return nextPage;
    }

    //관리자 페이지 가기
    @GetMapping("/manager")
    public String manager(HttpSession session,Model model){
        System.out.println("[mainController] manager");
        String nextPage="manager/manager";
        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기

        int isAdmin = loginedMemberDTO.getM_isAdmin();
        if (isAdmin != 1) { //관리자가 아닐 경우
            model.addAttribute("result", "실패");
            model.addAttribute("reason", "관리자만 관리자 화면에 접근 가능합니다.");
            nextPage = "/result";
        }
        else {
            List<MemberDTO> MemberDTOs= MemberService.getMember(); //맴버 서비스로부터 MemberDTO를 받아온다.
            model.addAttribute("MemberDTOs",MemberDTOs);
            List <GiftDTO> GiftDTOs= GiftService.getGift(); //선물 서비스로부터 giftDTO를 받아온다.
            model.addAttribute("GiftDTOs",GiftDTOs);
            List<SpecialGiftDTO> SpecialGiftDTOs=MyPageService.getSpecialGift(); //특별 선물도 받아온다.
            model.addAttribute("SpecialGiftDTOs",SpecialGiftDTOs);
        }

        return nextPage;
    }




}

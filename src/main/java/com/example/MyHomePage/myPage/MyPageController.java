package com.example.MyHomePage.myPage;


import com.example.MyHomePage.item.ItemDTO;
import com.example.MyHomePage.item.ItemService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.MyHomePage.Memeber.MemberDTO;
import com.example.MyHomePage.Memeber.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class MyPageController {
    @Autowired
    MemberService MemberService;
    @Autowired
    ItemService ItemService;
    @Autowired
    MyPageService MyPageService;

    //마이 ㅍ이지 보기
    @GetMapping("/myPage")
    public String myPage(Model model, HttpSession session){
        log.info("[MyPageController] myPage");
        String nextPage = "myPage/myPage";
        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기
        MemberDTO MemberDTO= MemberService.loginConfirm(loginedMemberDTO.getM_no()); //맴버 서비스로부터 MemberDTO를 받아온다.
        List<LikePointDTO> LikePointDTOs=ItemService.getLikePoint(loginedMemberDTO.getM_no()); //호감도 받아오기
        List<ItemDTO> MyItems=ItemService.getMyItem(loginedMemberDTO.getM_no()); //보유 선물 목록 받아오기
        List<SpecialGiftDTO> SpecialGiftDTOs=ItemService.getMySpecialItem(loginedMemberDTO.getM_no()); //보유 특별 선물 목록 받아오기


        model.addAttribute("MemberDTO",MemberDTO);
        model.addAttribute("MyItems", MyItems);
        model.addAttribute("SpecialGiftDTOs", SpecialGiftDTOs);
        model.addAttribute("LikePointDTOs", LikePointDTOs);
        return nextPage;
    }

    //맴버 수정용 (멤버)
    @PostMapping("/myPage/editMember")
    public String editMember(MemberDTO memberDTO){
        log.info("[MyPageController] editMember");
        String nextPage="redirect:/myPage";

        MemberService.editMember(memberDTO); //맴버 정보 수정된 거 업데이트

        return nextPage;
    }

    //특별 선물 추가하기
    @PostMapping("/SpecialGift/addMySpecialGift")
    public String addMySpecialGift(@RequestParam("sg_giftImage") MultipartFile file, HttpSession session) throws Exception{
        log.info("[SpecialGiftController] addMySpecialGift");
        String nextPage="redirect:/myPage";

        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\SpecialGiftImage"; //이미지 저장될 경로

        UUID uuid = UUID.randomUUID(); //UUID (파일 식별자) 생성
        String fileName = uuid + "_" + file.getOriginalFilename(); //파일 명 생성

        File saveFile = new File(projectPath, fileName); //파일 생성.
        file.transferTo(saveFile); //파일 저장

        SpecialGiftDTO specialGiftDTO = new SpecialGiftDTO(); //DB에 저장 할 특별 선물 DTO
        specialGiftDTO.setSg_name(loginedMemberDTO.getM_name() + "의 특별 선물");
        specialGiftDTO.setSg_filePath("SpecialGiftImage\\" + fileName);
        specialGiftDTO.setSg_ownner(loginedMemberDTO.getM_name());

        if(MyPageService.checkSomeonesSpecialGift(loginedMemberDTO.getM_name())){ //만약 이전에 저장된 특별 선물이 없을 경우
            MyPageService.saveSpecialGift(specialGiftDTO); //새로 특별 선물 저장
        }
        else{//이전에 저장된 특별 선물이 있으면
            //기존 것 수정
            MyPageService.editSpecialGift(specialGiftDTO);
        }
        return nextPage;
    }

    //특별 선물 삭제하기
    @GetMapping("/SpecialGift/deleteSpecialGift")
    public String deleteSpecialGift(@RequestParam("sg_no") int sg_no) throws Exception{
        log.info("[SpecialGiftController] deleteSpecialGift");
        String nextPage="redirect:/manager";
        MyPageService.deleteSpecialGift(sg_no);

        return nextPage;
    }
}

package com.example.MyHomePage.gift;


import com.example.MyHomePage.Memeber.MemberDTO;
import com.example.MyHomePage.item.ItemService;
import com.example.MyHomePage.myPage.LikePointDTO;
import com.example.MyHomePage.myPage.MyPageService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Random;


@Controller
@Slf4j
public class GiftController {
    @Autowired
    GiftService GiftService;
    @Autowired
    ItemService ItemService;
    @Autowired
    com.example.MyHomePage.Memeber.MemberService MemberService;
    @Autowired
    MyPageService MyPageService;

    //선물 게시판 매핑
    @GetMapping("/gift")
    public String gift(Model model, HttpSession session){
        log.info("[GiftController] gift");
        String nextPage = "gift/gift";
        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //세션으로부터 로그인 정보 받아오기
        List<GiftDTO> GiftDTOs=GiftService.getGift(); //전체 선물 목록 받아오기
        List<LikePointDTO> LikePointDTOs=ItemService.getLikePoint(loginedMemberDTO.getM_no()); //호감도 받아오기
        MemberDTO newloginedMemberDTO=MemberService.loginConfirm(loginedMemberDTO.getM_no()); //서비스로부터(DB) 로그인 정보를 받아온다.
        session.setAttribute("loginedMemberDTO",newloginedMemberDTO);//코인 갱신
        session.setMaxInactiveInterval(60*30);

        //모델로 넘겨주기
        model.addAttribute("GiftDTOs", GiftDTOs);
        model.addAttribute("Coin", loginedMemberDTO.getM_coin());
        model.addAttribute("LikePointDTOs", LikePointDTOs);

        return nextPage;
    }

    //선물 뽑기
    @PostMapping("/gift/giftToGet")
    public String giftToGet(HttpSession session, Model model){
        log.info("[GiftController] giftToGet");
        String nextPage = "redirect:/gift";
        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기
        int m_coin=loginedMemberDTO.getM_coin(); //현재 코인 가져오기
        int m_no=loginedMemberDTO.getM_no(); //사용자 번호 가져오기
        if(loginedMemberDTO.getM_coin()>=1){//코인이 있다면
            List<GiftDTO> GiftDTOs=GiftService.getGift(); //선물 목록 받아오기
            Random random=new Random();
            int ranNum=random.nextInt(GiftDTOs.size());

            //코인 차감
            int result=MemberService.subCoin(m_coin-1,m_no);
            if(result==1){
                String winningGift=GiftDTOs.get(ranNum).getG_name(); //랜덤 상품 지급
                int itemAddResult=ItemService.addItem(winningGift,m_no);

                if(itemAddResult!=1){ //아이템 저장 실패시
                    nextPage="/result";
                    model.addAttribute("result","실패");
                    model.addAttribute("reason","아이템 뽑기 실패. 다시 시도해주세요.");
                    MemberService.subCoin(m_coin,m_no);
                }
                else{

                }
            }
        }
        else{
            System.out.println("코인이 없습니다!");
        }

        return nextPage;
    }

    @PostMapping("/gift/AddGift")
    public String giftAdd(GiftDTO giftDTO,Model model){
        log.info("[GiftController] AddGift");
        String nextPage = "redirect:/manager";
        int AddGift= GiftService.AddGift(giftDTO);
        if(AddGift==-1){ //선물 추가 실패했을 경우
            model.addAttribute("result","실패");
            model.addAttribute("reason","선물 추가 실패");
            nextPage = "/result";

        }
        return nextPage;
    }

    //선물 지우기
    @GetMapping("/gift/giftDelete")
    public String giftDelete(@RequestParam(value="g_no") String g_no,Model model){
        log.info("[GiftController] giftDelete");
        String nextPage = "redirect:/manager";
        int giftDelete= GiftService.giftDelete(g_no);
        if(giftDelete==-1){ //선물 추가 실패했을 경우
            model.addAttribute("result","실패");
            model.addAttribute("reason","선물 추가 실패");
            nextPage = "/result";
        }
        return nextPage;
    }

    //호감도 달성으로 특전 선물 받기
    @PostMapping("/gift/specialGiftToGet")
    public String specialGiftToGet(LikePointDTO LikePointDTO,Model model, HttpSession session){
        log.info("[GiftController] specialGiftToGet");
        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기

        String nextPage = "result";
        if(LikePointDTO.getPoint()>=2){
            if(ItemService.checkHaveSpecialGift(LikePointDTO.getName(),loginedMemberDTO.getM_no()) != 1){ //만약 특별 선물을 받은 적 없다면
                if(MyPageService.checkSomeonesSpecialGift(LikePointDTO.getName())){
                    model.addAttribute("result","실패");
                    model.addAttribute("reason",LikePointDTO.getName()+"님은 아직 특별 선물을 등록하지 않았어요.");
                }
                else{
                    model.addAttribute("result","성공");
                    model.addAttribute("reason","선물 획득");
                    ItemService.addSpecialItem(LikePointDTO.getName(),loginedMemberDTO.getM_no()); //특별 선물 받기
                }



            }
            else{
                model.addAttribute("result","실패");
                model.addAttribute("reason","이미 획득한 선물입니다.");
            }

        }
        else{ //선물 추가 실패했을 경우
            model.addAttribute("result","실패");
            model.addAttribute("reason","호감도가 부족합니다.");

        }
        return nextPage;
    }
}

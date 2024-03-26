package com.example.MyHomePage.board;

import com.example.MyHomePage.Memeber.MemberDTO;
import com.example.MyHomePage.Memeber.MemberService;
import com.example.MyHomePage.gift.GiftDTO;
import com.example.MyHomePage.gift.GiftService;

import com.example.MyHomePage.item.ItemDTO;
import com.example.MyHomePage.item.ItemService;
import jakarta.servlet.http.HttpSession;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
public class BoardController {
    @Autowired
    BoardService BoardService;
    @Autowired
    GiftService GiftService;
    @Autowired
    MemberService MemberService;

    @Autowired
    ItemService ItemService;

    //게시판 매핑
    @GetMapping("/board")
    public String board(Model model){
        log.info("[BoardController] board");
        String nextPage = "board/board";
        List<BoardDTO> boardDTOs=BoardService.getPost(0,9); //글 목록 받아오기

        model.addAttribute("Posts", boardDTOs); //모델로 넘겨주기

        return nextPage;
    }

    @GetMapping("/board/boardToPost")
    public String toPost(Model model,HttpSession session){
        log.info("[BoardController] toPost");
        String nextPage = "board/boardToPost";
        List <MemberDTO> MemberDTOs= MemberService.getMember(); //맴버 서비스로부터 MemberDTO를 받아온다.
        model.addAttribute("MemberDTOs",MemberDTOs);

        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기
        List <ItemDTO> ItemDTOs= ItemService.getMyItem(loginedMemberDTO.getM_no()); //아이템 서비스로부터 가지고 있는 giftDTO를 받아온다.
        model.addAttribute("ItemDTOs",ItemDTOs);

        return nextPage;
    }

    @PostMapping("/board/toPost_Confirm")
    public String toPost_Confirm(BoardDTO boardDTO, HttpSession session,Model model){
        log.info("[BoardController] toPost_Confirm");
        model.addAttribute("result","성공");
        model.addAttribute("reason","글 작성 성공");
        String nextPage="/result";

        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기
        int result=BoardService.PostConfirm(boardDTO,loginedMemberDTO.getM_id()); //서비스로부터 글 쓴거 성공 여부를 받아온다.
            if(result==1) {
                //글 작성에 성공한 경우, 소지 아이템을 삭제하고, 호감도를 상승 또는 유지 시킨다.
                //사용자가 가지고 있는 아이템 중 사용한 아이템과 동일한 이름의 아이템을 전부 가져온다. 이때, 정렬은 획득한 시간 순서로 한다.
                ItemDTO ItemDTO= ItemService.getMyItem(loginedMemberDTO.getM_no(),boardDTO.getB_gift()); //아이템 서비스로부터 가지고 있는 giftDTO를 받아온다.
                int useItemNo=ItemDTO.getI_no(); //업데이트 할 아이템 번호
                int likePoint=0; //상승할 호감도

                //호감도 여부
                GiftDTO GiftDTO= GiftService.getGift(boardDTO.getB_gift()); //아이템 서비스로부터 가지고 있는 giftDTO를 받아온다.
                if(Objects.equals(GiftDTO.getG_giftOwner(), boardDTO.getB_receiver())){ //만약 호감도 불품이 맞다면
                    likePoint++; //호감도 상승
                }
                ItemService.useItem(useItemNo,boardDTO.getB_receiver(),likePoint); //아이템 업데이트 (업데이트 할 아이템 번호, 누가 받았는지, 호감도는 올랐는지는 업데이트)
            }
            else {
                //만약 실패시 실패 페이지를 보여준다.
                model.addAttribute("result","실패");
                model.addAttribute("reason","글 작성 실패. 다시 시도해주세요.");
                log.info("posting faill");
        }


        return nextPage;
    }

    @GetMapping("/board/boardDeail")
    public String boardDeail(@RequestParam(value="b_no") int b_no, Model model, HttpSession session){
        log.info("[BoardController] boardDeail");
        String nextPage = "board/boardDetail";

        BoardDTO boardDTO=BoardService.getPostByb_no(b_no); //글 받아오기

        //조회수 기능. (글 작성자가 보는 건 카운트 안함)
        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기
        if(!Objects.equals(loginedMemberDTO.getM_name(), boardDTO.getM_name())) {
            BoardService.addViewNum(boardDTO.getB_viewnum() + 1,b_no);
        }
        model.addAttribute("boardDTO",boardDTO);
        return nextPage;
    }

    @PostMapping("/board/boardToUpdatePost")
    public String boardToUpdatePost(@RequestParam(value="b_no") int b_no,
                                    @RequestParam(value="b_name") String b_name,
                                    @RequestParam(value="m_name") String m_name,
                                    @RequestParam(value="b_receiver") String b_receiver,
                                    @RequestParam(value="b_gift") String b_gift,
                                    @RequestParam(value="b_reg_date") String b_reg_date,
                                    @RequestParam(value="b_context") String b_context,
                                    Model model,HttpSession session){
        log.info("[BoardController] boardToUpdatePost");
        String nextPage = "/board/boardToUpdatePost";

        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기

        //글 수정 권한이 있나 확인
        if(m_name.equals(loginedMemberDTO.getM_name())){
            model.addAttribute("b_no",b_no);
            model.addAttribute("b_name",b_name);
            model.addAttribute("m_name",m_name);
            model.addAttribute("b_receiver",b_receiver);
            model.addAttribute("b_gift",b_gift);
            model.addAttribute("b_reg_date",b_reg_date);
            model.addAttribute("b_context",b_context);
        }
        else {   //수정 권한이 없을 경우
            model.addAttribute("result","실패");
            model.addAttribute("reason","글 수정 실패 (수정 권한 없음)");
        }
        return nextPage;
    }

    @PostMapping("/board/boardToUpdatePost_Confirm")
    public String boardToUpdatePost_Confirm(BoardDTO boardDTO,Model model){
        log.info("[BoardController] boardToUpdatePost_Confirm");
        String nextPage="/result";

        int result=BoardService.PostUpdateConfirm(boardDTO); //서비스로부터 글 수정 성공 여부를 받아온다.
        if(result == 1){
            model.addAttribute("result","성공");
            model.addAttribute("reason","글 수정 성공");

        }
        else {   //수정 실패시
            model.addAttribute("result","실패");
            model.addAttribute("reason","글 수정 실패");

        }
        return nextPage;
    }

    @PostMapping("/board/boardToDeletePost")
    public String boardToDeletePost(HttpSession session, BoardDTO boardDTO,Model model){
        log.info("[BoardController] boardToDeletePost");
        String nextPage = "redirect:/board";
        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기
        //글 삭제 권한이 있나 확인
        if(boardDTO.getM_name().equals(loginedMemberDTO.getM_name())) {
            int result = BoardService.DeletePost(boardDTO.getB_no()); //서비스로부터 글 삭제 성공 여부를 받아온다.
            if (result == -1) {
                //만약 수정 실패시 실패 페이지를 보여준다.
                model.addAttribute("result","실패");
                model.addAttribute("reason","글 삭제 실패 (오류. 다시 시도해주세요.)");
                nextPage="/result";
            }
        }
        else {   //삭제 실패시
            model.addAttribute("result","실패");
            model.addAttribute("reason","글 삭제 실패 (권한 없음)");
            nextPage="/result";
        }
        return nextPage;

    }
}

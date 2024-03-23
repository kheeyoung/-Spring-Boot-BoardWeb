package com.example.MyHomePage.board;

import com.example.MyHomePage.Memeber.MemberDTO;
import com.example.MyHomePage.Memeber.MemberService;
import com.example.MyHomePage.gift.GiftDTO;
import com.example.MyHomePage.gift.GiftService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BoardController {
    @Autowired
    BoardService BoardService;
    @Autowired
    GiftService GiftService;

    @Autowired
    MemberService MemberService;
    //게시판 매핑
    @GetMapping("/board")
    public String board(Model model){
        System.out.println("[BoardController] board");
        String nextPage = "board/board";
        List<BoardDTO> boardDTOs=BoardService.getPost(0,9); //글 목록 받아오기

        model.addAttribute("Posts", boardDTOs); //모델로 넘겨주기

        return nextPage;
    }

    @GetMapping("/board/boardToPost")
    public String toPost(Model model){
        System.out.println("[BoardController] boardToPost");
        String nextPage = "board/boardToPost";
        List <MemberDTO> MemberDTOs= MemberService.getMember(); //맴버 서비스로부터 MemberDTO를 받아온다.
        model.addAttribute("MemberDTOs",MemberDTOs);

        List <GiftDTO> GiftDTOs= GiftService.getGift(); //선물 서비스로부터 giftDTO를 받아온다.
        model.addAttribute("GiftDTOs",GiftDTOs);

        return nextPage;
    }

    @PostMapping("/board/toPost_Confirm")
    public String toPost_Confirm(BoardDTO boardDTO, HttpSession session,Model model){
        System.out.println("[BoardController] toPost_Confirm");
        model.addAttribute("result","성공");
        model.addAttribute("reason","글 작성 성공");
        String nextPage="/result";

        MemberDTO loginedMemberDTO=(MemberDTO) session.getAttribute("loginedMemberDTO"); //로그인 정보 받아오기

        int result=BoardService.PostConfirm(boardDTO,loginedMemberDTO.getM_id()); //서비스로부터 글 쓴거 성공 여부를 받아온다.
        if(result == -1){
            //만약 실패시 실패 페이지를 보여준다.
            model.addAttribute("result","실패");
            model.addAttribute("reason","글 작성 실패. 다시 시도해주세요.");
            System.out.println("posting faill");
        }
        return nextPage;
    }

    @GetMapping("/board/boardDeail")
    public String boardDeail(@RequestParam(value="b_no") int b_no, Model model){
        System.out.println("[BoardController] boardDeail");
        String nextPage = "board/boardDetail";

        BoardDTO boardDTO=BoardService.getPostByb_no(b_no); //글 받아오기
        System.out.println(boardDTO.getB_name());
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
        System.out.println("[BoardController] boardToUpdatePost");
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

            System.out.println(b_name+" / "+m_name+" / "+b_receiver+" / "+b_gift+" / "+b_reg_date+" / "+b_context);

        }
        else {   //수정 권한이 없을 경우
            model.addAttribute("result","실패");
            model.addAttribute("reason","글 수정 실패 (수정 권한 없음)");
        }

        return nextPage;
    }

    @PostMapping("/board/boardToUpdatePost_Confirm")
    public String boardToUpdatePost_Confirm(BoardDTO boardDTO,Model model){
        System.out.println("[BoardController] boardToUpdatePost");
        String nextPage="/result";
        System.out.println("name: "+boardDTO.getB_name()+" context: "+boardDTO.getB_context());

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
        System.out.println("[BoardController] boardToDeletePost");
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

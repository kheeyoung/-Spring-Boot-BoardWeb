package com.example.MyHomePage.board;

import com.example.MyHomePage.Memeber.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BoardService {
    @Autowired BoardDao boardDao;
    @Autowired MemberService MemberService;

    //포스팅 관련 변수
    final static public int SUCCESS=1;
    final static public int FAIL=-1;

    public List<BoardDTO> getPost(int fristPost, int lastPost) {
        log.info("[BoardService] getPost");
        List<BoardDTO> boardDTOS =boardDao.selectPost();
        List<BoardDTO> subBoardDTOS;
        if(boardDTOS.size()>lastPost){
            subBoardDTOS=boardDTOS.subList(fristPost, lastPost);
        }
        else{
            subBoardDTOS=boardDTOS.subList(fristPost, boardDTOS.size());
        }


        return subBoardDTOS;
    }

    public int PostConfirm(BoardDTO boardDTO, int b_writer_m_no) {
        log.info("[BoardService] PostConfirm");
        //번호로 글쓴이/받는이 이름,번호 받아오기 (글쓴이는 이름,번호 / 받는 이르는 번호 세팅 필요)
        int b_receiver_m_no= MemberService.getMemberByName(boardDTO.getB_receiver_m_name()).getM_no();
        String b_writer_m_name=MemberService.getMemberByNo(b_writer_m_no).getM_name();
        //이름 세팅하기
        boardDTO.setB_receiver_m_no(b_receiver_m_no);
        boardDTO.setB_writer_m_name(b_writer_m_name);
        boardDTO.setB_writer_m_no(b_writer_m_no);
        int result = boardDao.PostConfirm(boardDTO);
        if (result == 1) { //포스팅 성공시
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public BoardDTO getPostByb_no(int b_No) {
        log.info("[BoardService] getPostByb_no");
        return boardDao.selectPost(b_No);
    }

    public int PostUpdateConfirm(BoardDTO boardDTO) {
        log.info("[BoardService] PostUpdateConfirm");
        int result = boardDao.PostUpdateConfirm(boardDTO, boardDTO.getB_no());
        if (result == 1) { //포스팅 수정 성공시
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public int DeletePost(int b_no) {
        log.info("[BoardService] DeletePost");
        int result = boardDao.DeletePost(b_no);
        if (result == 1) { //포스팅 삭제 성공시
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public void addViewNum(int i, int b_no) {
        log.info("[BoardService] addViewNum");
        boardDao.addViewNum(i,b_no);
    }
}

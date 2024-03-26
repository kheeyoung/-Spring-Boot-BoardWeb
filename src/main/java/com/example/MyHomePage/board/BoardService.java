package com.example.MyHomePage.board;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BoardService {
    @Autowired BoardDao boardDao;

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

    public int PostConfirm(BoardDTO boardDTO, String username) {
        log.info("[BoardService] PostConfirm");
        int result = boardDao.PostConfirm(boardDTO, username);
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

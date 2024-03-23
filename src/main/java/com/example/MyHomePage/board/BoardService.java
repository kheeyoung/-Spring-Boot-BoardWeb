package com.example.MyHomePage.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    @Autowired BoardDao boardDao;

    //포스팅 관련 변수
    final static public int SUCCESS=1;
    final static public int FAIL=-1;

    public List<BoardDTO> getPost(int fristPost, int lastPost) {
        System.out.println("[BoardService] getPost");
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
        System.out.println("[BoardService] PostConfirm");
        int result = boardDao.PostConfirm(boardDTO, username);
        if (result == 1) { //포스팅 성공시
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public BoardDTO getPostByb_no(int b_No) {
        System.out.println("[BoardService] getPostByb_no");
        return boardDao.selectPost(b_No);
    }

    public int PostUpdateConfirm(BoardDTO boardDTO) {
        System.out.println("[BoardService] PostUpdateConfirm");
        int result = boardDao.PostUpdateConfirm(boardDTO, boardDTO.getB_no());
        if (result == 1) { //포스팅 수정 성공시
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public int DeletePost(int b_no) {
        System.out.println("[BoardService] DeletePost");
        int result = boardDao.DeletePost(b_no);
        if (result == 1) { //포스팅 삭제 성공시
            return SUCCESS;
        } else {
            return FAIL;
        }
    }
}

package com.example.MyHomePage.Memeber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MemberService {
    @Autowired
    MemberDao MemberDao;

    //회원가입 관련 변수
    final static public int MEMBER_ACCOUNT_ALREADY_EXIST=0;
    final static public int MEMBER_ACCOUNT_CREATE_SUCCESS=1;
    final static public int MEMBER_ACCOUNT_CREATE_FAIL=-1;

    //맴버 로그인 처리 기능
    public MemberDTO loginConfirm(MemberDTO memberDTO) {
        log.info("[MemberService] loginConfirm");
        MemberDTO loginedMemberDTO=MemberDao.selectMember(memberDTO); //Dao로 부터 로그인 된 값을 가져온다.
        if(loginedMemberDTO!=null){
            log.info("[MemberService] loginConfirm : !!로그인 성공!!");
        }
        else {
            log.info("[MemberService] loginConfirm : 로그인 실패!");
        }

        return loginedMemberDTO;
    }

    //회원가입
    public int signIn(MemberDTO memberDTO) {
        log.info("[MemberService] signIn");
        boolean isMember=MemberDao.isMember(memberDTO.getM_id());
        if(!isMember){
            int result=MemberDao.insertMemberAccount(memberDTO);
            if(result>0){
                return MEMBER_ACCOUNT_CREATE_SUCCESS;
            }
            else {
                return MEMBER_ACCOUNT_CREATE_FAIL;
            }
        }
        else {
            return MEMBER_ACCOUNT_ALREADY_EXIST;
        }
    }

    public List<MemberDTO> getMember() {
        log.info("[MemberService] getMember");
        return MemberDao.getMember();
    }
    public List<String> getMemberId() { //멤버 이름 가져오기
        log.info("[MemberService] getMemberId");
        List<MemberDTO> MemberDTOs=MemberDao.getMember();
        List<String>MemberName=new ArrayList<String>();
        for (MemberDTO memberDTO : MemberDTOs) {
            MemberName.add(memberDTO.getM_id());
        }
        return MemberName;
    }

    public void editMember(MemberDTO memberDTO) {
        log.info("[MemberService] editMember");
        MemberDao.editMember(memberDTO);
    }

    public int subCoin(int m_coin, int m_no) {
        log.info("[MemberService] subCoin");
        return MemberDao.subCoin(m_coin,m_no);
    }

    public MemberDTO loginConfirm(int m_no) {
        log.info("[MemberService] loginConfirm");
        MemberDTO loginedMemberDTO=MemberDao.selectMember(m_no); //Dao로 부터 로그인 된 값을 가져온다.
        if(loginedMemberDTO!=null){
            log.info("[MemberService] loginConfirm : !!로그인 성공!!");
        }
        else {
            log.info("[MemberService] loginConfirm : 로그인 실패!");
        }
        return loginedMemberDTO;
    }
}

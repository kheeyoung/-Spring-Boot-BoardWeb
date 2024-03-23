package com.example.MyHomePage.Memeber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    MemberDao MemberDao;

    //회원가입 관련 변수
    final static public int MEMBER_ACCOUNT_ALREADY_EXIST=0;
    final static public int MEMBER_ACCOUNT_CREATE_SUCCESS=1;
    final static public int MEMBER_ACCOUNT_CREATE_FAIL=-1;

    //맴버 로그인 처리 기능
    public MemberDTO loginConfirm(MemberDTO memberDTO) {
        System.out.println("[MemberService] loginConfirm");
        MemberDTO loginedMemberDTO=MemberDao.selectMember(memberDTO); //Dao로 부터 로그인 된 값을 가져온다.
        if(loginedMemberDTO!=null){
            System.out.println("[MemberService] loginConfirm : !!로그인 성공!!");
        }
        else {
            System.out.println("[MemberService] loginConfirm : 로그인 실패!");
        }

        return loginedMemberDTO;
    }

    //회원가입
    public int signIn(MemberDTO memberDTO) {
        System.out.println("[MemberService] signIn");
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
        System.out.println("[MemberService] getMember");
        return MemberDao.getMember();
    }

    public void editMember(MemberDTO memberDTO) {
        System.out.println("[MemberService] editMember");
        MemberDao.editMember(memberDTO);
    }

    public int subCoin(int m_coin, int m_no) {
        System.out.println("[MemberService] subCoin");
        return MemberDao.subCoin(m_coin,m_no);
    }

    public MemberDTO loginConfirm(int m_no) {
        System.out.println("[MemberService] loginConfirm");
        MemberDTO loginedMemberDTO=MemberDao.selectMember(m_no); //Dao로 부터 로그인 된 값을 가져온다.
        if(loginedMemberDTO!=null){
            System.out.println("[MemberService] loginConfirm : !!로그인 성공!!");
        }
        else {
            System.out.println("[MemberService] loginConfirm : 로그인 실패!");
        }

        return loginedMemberDTO;
    }
}

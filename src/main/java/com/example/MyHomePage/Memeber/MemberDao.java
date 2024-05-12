package com.example.MyHomePage.Memeber;


import com.example.MyHomePage.board.BoardDTO;
import jdk.jfr.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MemberDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    //로그인 처리
    public MemberDTO selectMember(MemberDTO memberDTO) {
        log.info("[MemberDao] selectMember");

        String sql="SELECT * FROM kim_tbl_member WHERE m_id="+memberDTO.getM_id();
        List<MemberDTO> memberDTOs =new ArrayList<MemberDTO>();

        try{
            RowMapper<MemberDTO> rowMapper= BeanPropertyRowMapper.newInstance(MemberDTO.class);
            memberDTOs=jdbcTemplate.query(sql,rowMapper);
            if(memberDTOs.isEmpty() || !memberDTO.getM_pw().equals(memberDTOs.get(0).getM_pw())){
                memberDTOs.clear();
                log.info("clear");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return !memberDTOs.isEmpty() ? memberDTOs.get(0):null;
    }

    //가입 여부 알아내기
    public boolean isMember(String m_id) {
        log.info("[MemberDao] isMember");

        String sql="SELECT COUNT(*) FROM kim_tbl_member WHERE m_id=?";
        int result=0;
        try {
            result = jdbcTemplate.queryForObject(sql, Integer.class, m_id);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result>0 ? true:false;
    }

    //회원가입하기.
    public int insertMemberAccount(MemberDTO memberDTO) {
        log.info("[MemberDao] isMember");
        String sql="INSERT INTO kim_tbl_member (m_id,m_pw,m_name,m_gender,m_mail,m_phone,m_reg_date,m_mod_date,m_coin) VALUE (?,?,?,?,?,?,NOW(),NOW(),0)";

        int result=-1;
        try {
            result=jdbcTemplate.update(sql,memberDTO.getM_id(),memberDTO.getM_pw(),memberDTO.getM_name(),memberDTO.getM_gender(),memberDTO.getM_mail(),memberDTO.getM_phone());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //멤버 전부 가져오기
    public List <MemberDTO> getMember() {
        log.info("[MemberDao] getMember");
        String sql="SELECT * FROM kim_tbl_member;";
        List <MemberDTO> MemberDTOs=new ArrayList<MemberDTO>();
        try {
            RowMapper<MemberDTO> rowMapper= BeanPropertyRowMapper.newInstance(MemberDTO.class);
            MemberDTOs=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return MemberDTOs;


    }


    public void editMember(MemberDTO memberDTO) {
        log.info("[MemberDao] editMember");
        String sql="UPDATE kim_tbl_member SET m_isAdmin="+memberDTO.getM_isAdmin()+"," +
                                            " m_id='" +memberDTO.getM_id()+"'," +
                                            " m_pw='" +memberDTO.getM_pw()+"'," +
                                            " m_name='" +memberDTO.getM_name()+"'," +
                                            " m_gender='" +memberDTO.getM_gender()+"'," +
                                            " m_mail='" +memberDTO.getM_mail()+"'," +
                                            " m_phone='" +memberDTO.getM_phone()+"'," +
                                            " m_mod_date=NOW()," +
                                            " m_coin=" +memberDTO.getM_coin()+
                                            " WHERE m_no="+memberDTO.getM_no()+";";
        try {
            jdbcTemplate.update(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public int subCoin(int m_coin,int m_no) {
        log.info("[MemberDao] subCoin");
        String sql="UPDATE kim_tbl_member SET m_coin=" +m_coin+", m_mod_date=NOW()" +
                " WHERE m_no="+m_no+";";
        try {
            jdbcTemplate.update(sql);
            return 1;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    public MemberDTO selectMember(int m_no) {
        log.info("[MemberDao] selectMember");

        String sql="SELECT * FROM kim_tbl_member WHERE m_no="+m_no;
        List<MemberDTO> memberDTOs =new ArrayList<MemberDTO>();

        try{
            RowMapper<MemberDTO> rowMapper= BeanPropertyRowMapper.newInstance(MemberDTO.class);
            memberDTOs=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return !memberDTOs.isEmpty() ? memberDTOs.get(0):null;
    }

    public MemberDTO getMemberByName(String m_name) {
        String sql="SELECT * FROM kim_tbl_member WHERE m_name="+m_name;
        List<MemberDTO> memberDTOs =new ArrayList<MemberDTO>();

        try{
            RowMapper<MemberDTO> rowMapper= BeanPropertyRowMapper.newInstance(MemberDTO.class);
            memberDTOs=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return !memberDTOs.isEmpty() ? memberDTOs.get(0):null;
    }

    public MemberDTO getMemberByNo(int m_no) {
        String sql="SELECT * FROM kim_tbl_member WHERE m_no="+m_no;
        List<MemberDTO> memberDTOs =new ArrayList<MemberDTO>();

        try{
            RowMapper<MemberDTO> rowMapper= BeanPropertyRowMapper.newInstance(MemberDTO.class);
            memberDTOs=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return !memberDTOs.isEmpty() ? memberDTOs.get(0):null;
    }
}

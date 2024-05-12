package com.example.MyHomePage.myPage;

import com.example.MyHomePage.Memeber.MemberDTO;
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
public class MyPageDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    //자신의 특별 선물 추가하기
    public void saveSpecialGift(SpecialGiftDTO specialGiftDTO) {
        log.info("[MyPageDao] saveSpecialGift");
        String sql="INSERT INTO kim_tbl_specialGift (sg_name,sg_filePath,sg_ownner_no,sg_reg_date,sg_mod_date) VALUE (?,?,?,NOW(),NOW())";

        try {
            jdbcTemplate.update(sql,specialGiftDTO.getSg_name(),specialGiftDTO.getSg_filePath(),specialGiftDTO.getSg_ownner_no());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //특별 선물 전부 얻기
    public List<SpecialGiftDTO> getSpecialGift() {
        log.info("[MyPageDao] getSpecialGift");
        String sql="SELECT * FROM kim_tbl_specialGift;";

        List<SpecialGiftDTO> SpecialGiftDTOs =new ArrayList<SpecialGiftDTO>();

        try{
            RowMapper<SpecialGiftDTO> rowMapper= BeanPropertyRowMapper.newInstance(SpecialGiftDTO.class);
            SpecialGiftDTOs=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return SpecialGiftDTOs;
    }

    //특정 멤버의 특별 선물이 등록 되어 있나 확인 (번호로 확인)
    public boolean checkSomeonesSpecialGift(int no) {
        log.info("[MyPageDao] checkSomeonesSpecialGift");
        String sql="SELECT * FROM kim_tbl_specialGift WHERE sg_name='"+no+"';";

        List<SpecialGiftDTO> SpecialGiftDTOs =new ArrayList<SpecialGiftDTO>();

        try{
            RowMapper<SpecialGiftDTO> rowMapper= BeanPropertyRowMapper.newInstance(SpecialGiftDTO.class);
            SpecialGiftDTOs=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return SpecialGiftDTOs.size()!=0 ? false:true;
    }

    //특정 멤버의 특별 선물이 등록 되어 있나 확인 (이름으로 확인)
    public boolean checkSomeonesSpecialGift(String name) {
        log.info("[MyPageDao] checkSomeonesSpecialGift");
        String sql="SELECT * FROM kim_tbl_specialGift WHERE sg_name ='"+name+"';";

        List<SpecialGiftDTO> SpecialGiftDTOs =new ArrayList<SpecialGiftDTO>();

        try{
            RowMapper<SpecialGiftDTO> rowMapper= BeanPropertyRowMapper.newInstance(SpecialGiftDTO.class);
            SpecialGiftDTOs=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return SpecialGiftDTOs.size()!=0 ? false:true;
    }

    //특별 선물 지우기
    public void deleteSpecialGift(int sg_no) {
        log.info("[MyPageDao] deleteSpecialGift");
        String sql="DELETE FROM kim_tbl_specialGift WHERE sg_no='"+sg_no+"';";

        try {
            jdbcTemplate.update(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void editSpecialGift(SpecialGiftDTO specialGiftDTO) {
        log.info("[MyPageDao] editSpecialGift");
        String sql="UPDATE kim_tbl_specialGift SET sg_filePath=?, sg_mod_date=NOW() WHERE sg_ownner_no=?;";

        try {
            jdbcTemplate.update(sql,specialGiftDTO.getSg_filePath(),specialGiftDTO.getSg_ownner_no());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

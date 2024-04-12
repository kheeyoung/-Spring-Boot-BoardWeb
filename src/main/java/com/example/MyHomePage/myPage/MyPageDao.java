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
    public void saveSpecialGift(SpecialGiftDTO specialGiftDTO) {
        log.info("[MyPageDao] saveSpecialGift");
        String sql="INSERT INTO kim_tbl_specialGift (sg_name,sg_filePath,sg_ownner,sg_reg_date,sg_mod_date) VALUE (?,?,?,NOW(),NOW())";

        try {
            jdbcTemplate.update(sql,specialGiftDTO.getSg_name(),specialGiftDTO.getSg_filePath(),specialGiftDTO.getSg_ownner());
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

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

    public boolean checkSomeonesSpecialGift(String name) {
        log.info("[MyPageDao] checkSomeonesSpecialGift");
        String sql="SELECT * FROM kim_tbl_specialGift WHERE sg_ownner='"+name+"';";

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
}

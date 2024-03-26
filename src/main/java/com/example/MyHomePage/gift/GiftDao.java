package com.example.MyHomePage.gift;


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
public class GiftDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    //전체 선물 조회
    public List<GiftDTO> getGift() {
        log.info("[GiftDao] getGift");
        String sql= "SELECT * FROM kim_tbl_gift";
        List<GiftDTO> giftDTOS=new ArrayList<GiftDTO>();
        try{
            RowMapper<GiftDTO> rowMapper= BeanPropertyRowMapper.newInstance(GiftDTO.class);
            giftDTOS=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return giftDTOS;
    }

    public GiftDTO getGift(String bGift) {
        log.info("[GiftDao] getGift");
        String sql= "SELECT * FROM kim_tbl_gift WHERE g_name='"+bGift+"'";
        List<GiftDTO> giftDTOS=new ArrayList<GiftDTO>();
        try{
            RowMapper<GiftDTO> rowMapper= BeanPropertyRowMapper.newInstance(GiftDTO.class);
            giftDTOS=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return giftDTOS.get(0);
    }


    public int AddGift(GiftDTO giftDTO) {
        log.info("[GiftDao] AddGift");
        String sql= "INSERT INTO kim_tbl_gift (g_name,g_giftOwner,g_reg_date,g_mod_date,g_about) VALUE (?,?,NOW(),NOW(),?)";
        int result=-1;
        try {
            result=jdbcTemplate.update(sql,giftDTO.getG_name(),giftDTO.getG_giftOwner(),giftDTO.getG_about());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public int giftDelete(String g_no) {
        log.info("[GiftDao] giftDelete");
        String sql= "DELETE FROM kim_tbl_gift WHERE g_no=?";
        int result=-1;
        try {
            result=jdbcTemplate.update(sql,g_no);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }



}


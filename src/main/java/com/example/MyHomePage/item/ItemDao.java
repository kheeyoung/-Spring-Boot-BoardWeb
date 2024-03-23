package com.example.MyHomePage.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemDao {
    @Autowired
    JdbcTemplate jdbcTemplate;


    public List<ItemDTO> getMyItem(int i_who_Have_m_no) {
        System.out.println("[ItemDao] getMyItem");
        String sql="SELECT * FROM kim_tbl_itemHave WHERE i_who_Have_m_no="+i_who_Have_m_no;
        List <ItemDTO> ItemDTOs=new ArrayList<ItemDTO>();
        try {
            RowMapper<ItemDTO> rowMapper= BeanPropertyRowMapper.newInstance(ItemDTO.class);
            ItemDTOs=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ItemDTOs;
    }

    public int addItme(String winningGift, int i_who_Have_m_no) {
        System.out.println("[ItemDao] addItme");
        String sql = "INSERT INTO kim_tbl_itemhave (i_name,i_who_Have_m_no,i_reg_date,i_mod_date) VALUE(?,?,NOW(),NOW())";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, winningGift, i_who_Have_m_no);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

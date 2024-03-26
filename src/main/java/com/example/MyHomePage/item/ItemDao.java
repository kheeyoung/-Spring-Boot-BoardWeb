package com.example.MyHomePage.item;

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
public class ItemDao {
    @Autowired
    JdbcTemplate jdbcTemplate;


    public List<ItemDTO> getMyItem(int i_who_Have_m_no) { //사용한 적 없는 가진 아이템들
        log.info("[ItemDao] getMyItem");
        String sql="SELECT * FROM kim_tbl_itemHave WHERE i_who_Have_m_no="+i_who_Have_m_no+" AND i_who_get IS NULL"; //가지고 있는 아이템 중에서 사용하지 않은 (=받은 사람이 없는) 아이템만 리턴
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

    public List<ItemDTO> getMyUsedItem(int i_who_Have_m_no) { //사용한 적 있는 가진 아이템들
        log.info("[ItemDao] getMyItem");
        String sql="SELECT * FROM kim_tbl_itemHave WHERE i_who_Have_m_no="+i_who_Have_m_no+" AND i_who_get IS NOT NULL"; //가지고 있는 아이템 중에서 사용한 (=받은 사람이 있는) 아이템만 리턴
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

    public ItemDTO getMyItem(int mNo, String bGift) {   //사용한 적 없는 가진 아이템 중 가장 오래된 것
        log.info("[ItemDao] getMyItem");
        String sql="SELECT * FROM kim_tbl_itemHave WHERE i_who_Have_m_no='"+mNo+"' AND i_name='"+bGift+"' AND i_who_get IS NULL ORDER BY i_reg_date"; //가지고 있는 아이템 중에서 사용하지 않은 (=받은 사람이 없는) 아이템만 리턴
        List <ItemDTO> ItemDTOs=new ArrayList<ItemDTO>();
        try {
            RowMapper<ItemDTO> rowMapper= BeanPropertyRowMapper.newInstance(ItemDTO.class);
            ItemDTOs=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ItemDTOs.get(0); //선택된 아이템 중 가장 처음 것 (=가장 오래된 것)을 리턴 한다.
    }

    public int addItme(String winningGift, int i_who_Have_m_no) {   //소유 아이템 추가하기
        log.info("[ItemDao] addItme");
        String sql = "INSERT INTO kim_tbl_itemhave (i_name,i_who_Have_m_no,i_reg_date,i_mod_date) VALUE(?,?,NOW(),NOW())";
        int result = -1;
        try {
            result = jdbcTemplate.update(sql, winningGift, i_who_Have_m_no);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    public void useItem(int useItemNo,String b_receiver, int likePoint) { //아이템 사용하기
        log.info("[ItemDao] useItem");
        String sql= "UPDATE kim_tbl_itemHave SET i_who_get= '"+b_receiver+"', i_get_point="+likePoint+", i_mod_date=NOW() WHERE i_no="+useItemNo;

        try {
            jdbcTemplate.update(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

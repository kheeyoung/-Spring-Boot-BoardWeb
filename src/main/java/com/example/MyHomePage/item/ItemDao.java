package com.example.MyHomePage.item;

import com.example.MyHomePage.myPage.LikePointDTO;
import com.example.MyHomePage.myPage.SpecialGiftDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ItemDao {
    @Autowired
    JdbcTemplate jdbcTemplate;


    //특정 멤버가 가진 아이템 가져오기 (m_no로)
    public List<ItemDTO> getMyItem(int i_who_Have_m_no) { //사용한 적 없는 가진 아이템들
        log.info("[ItemDao] getMyItem");
        String sql="SELECT * FROM kim_tbl_itemHave WHERE i_who_Have_m_no="+i_who_Have_m_no+" AND i_who_get IS NULL AND i_is_special IS NULL"; //가지고 있는 일반 아이템 중에서 사용하지 않은 (=받은 사람이 없는) 아이템만 리턴
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

    //특정 멤버가 사용한 적 있는 아이템 가져오기 (m_no로) > 호감도 계산용으로 쓰기 위함
    public List<ItemDTO> getMyUsedItem(int i_who_Have_m_no) { //사용한 적 있는 가진 아이템들 가져오기
        log.info("[ItemDao] getMyItem");
        String sql="SELECT * FROM kim_tbl_itemHave WHERE i_who_Have_m_no="+i_who_Have_m_no+" AND i_who_get IS NOT NULL AND i_is_special IS NULL"; //가지고 있는 아이템 중에서 사용한 (=받은 사람이 있는) 아이템만 리턴
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

    //특정 멤버가 가진 특정 아이템중 가장 오래된 아이템 가져오기 (m_no과 i_name으로)
    public ItemDTO getMyItem(int mNo, String bGift) {   //사용한 적 없는 가진 아이템 중 가장 오래된 것
        log.info("[ItemDao] getMyItem");
        String sql="SELECT * FROM kim_tbl_itemHave WHERE i_who_Have_m_no='"+mNo+"' AND i_name='"+bGift+"' AND i_who_get IS NULL ORDER BY i_reg_date AND i_is_special IS NULL"; //가지고 있는 아이템 중에서 사용하지 않은 (=받은 사람이 없는) 아이템만 리턴
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

    //특정 멤버가 가진 아이템 추가
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


    //특정 멤버가 아이템 사용하기
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

    //특정 멤버의 호감도 계산하기
    public List<LikePointDTO> getMyLikePoint(int m_no) {
        log.info("[ItemDao] getMyLikePoint");
        String sql= "SELECT i_who_get, SUM(i_get_point) FROM kim_tbl_itemhave WHERE i_who_Have_m_no=? AND i_who_get!='' GROUP BY i_who_get";
        List<LikePointDTO> LikePointDTOs=new ArrayList<LikePointDTO>();
        try {
            LikePointDTOs=jdbcTemplate.query(sql, new RowMapper<LikePointDTO>() {
                @Override
                public LikePointDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LikePointDTO LikePointDTO=new LikePointDTO();
                    LikePointDTO.setPoint(rs.getInt("SUM(i_get_point)"));

                    LikePointDTO.setName(rs.getString("i_who_get")); //이름을 받아온다.

                    return LikePointDTO;
                }
            },m_no);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return LikePointDTOs;
    }

    //특정 멤버에게 특별 아이템 가지게 하기
    public void addSpecialItme(String name, int m_no) {
        log.info("[ItemDao] addSpecialItme");
        String sql = "INSERT INTO kim_tbl_itemhave (i_name,i_who_Have_m_no,i_is_special,i_reg_date,i_mod_date) VALUE(?,?,?,NOW(),NOW())";
        jdbcTemplate.update(sql, name+"의 특별 선물", m_no,"1");

    }

    //특정 멤버가 다른 멤버의 특별 선물 가지고 있나 확인하기
    public int checkHaveSpecialGift(String name, int m_no) {
        log.info("[ItemDao] checkHaveSpecialGift");
        String sql="SELECT * FROM kim_tbl_itemHave WHERE i_who_Have_m_no="+m_no+" AND i_name='"+name+"의 특별 선물' AND i_is_special = '1'"; //가지고 있는 특별 아이템중 그 캐릭터의 특별 선물 찾기
        List <ItemDTO> ItemDTOs=new ArrayList<ItemDTO>();
        try {
            RowMapper<ItemDTO> rowMapper= BeanPropertyRowMapper.newInstance(ItemDTO.class);
            ItemDTOs=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ItemDTOs.size()>0 ? 1: 0;
    }

    //특정 멤버가 가지고 있는 특별 선물 전부 가져오기
    public List<SpecialGiftDTO> getMySpecialItem(int m_no) {
        log.info("[ItemDao] getMySpecialItem");
        String sql="SELECT sg_filePath, sg_name FROM kim_tbl_itemhave JOIN kim_tbl_specialgift ON kim_tbl_itemhave.i_name=kim_tbl_specialgift.sg_name \n" +
                "WHERE kim_tbl_itemhave.i_who_Have_m_no="+m_no+" AND kim_tbl_itemhave.i_is_special = '1'"; //가지고 있는 특별 선물 찾기
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

}

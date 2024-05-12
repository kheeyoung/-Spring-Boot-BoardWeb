package com.example.MyHomePage.board;

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
public class BoardDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public int PostConfirm(BoardDTO boardDTO) {
        log.info("[BoardDao] PostConfirm");
        String sql="INSERT INTO kim_tbl_boardpost " +
                "(b_name,b_writer_m_no,b_gift,b_receiver_m_no,b_viewnum,b_context,b_reg_date,b_mod_date,b_writer_m_name,b_receiver_m_name) " +
                "VALUE (?,?,?,?,0,?,NOW(),NOW(),?,?)";

        int result=-1;
        try {
            result=jdbcTemplate.update(sql,boardDTO.getB_name(),
                                                boardDTO.getB_writer_m_no(),
                                                boardDTO.getB_gift(),
                                                boardDTO.getB_receiver_m_no(),
                                                boardDTO.getB_context(),
                                                boardDTO.getB_writer_m_name(),
                                                boardDTO.getB_receiver_m_name());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //전체 글 조회
    public List<BoardDTO> selectPost() {
        log.info("[BoardDao] selectPost");

        String sql= "SELECT * FROM kim_tbl_boardpost";
        List<BoardDTO> boardDTOS=new ArrayList<BoardDTO>();
        try{
            RowMapper<BoardDTO> rowMapper= BeanPropertyRowMapper.newInstance(BoardDTO.class);
            boardDTOS=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return boardDTOS;
    }
    //특정 글 조회
    public BoardDTO selectPost(int b_no) {
        log.info("[BoardDao] selectPost");

        String sql= "SELECT * FROM kim_tbl_boardpost WHERE b_no="+b_no;
        List<BoardDTO> boardDTOS=new ArrayList<BoardDTO>();
        try{
            RowMapper<BoardDTO> rowMapper= BeanPropertyRowMapper.newInstance(BoardDTO.class);
            boardDTOS=jdbcTemplate.query(sql,rowMapper);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return boardDTOS.get(0);
    }

    public int PostUpdateConfirm(BoardDTO boardDTO, int b_no) {
        log.info("[BoardDao] PostUpdateConfirm");
        String sql="UPDATE kim_tbl_boardpost SET b_name= '"+boardDTO.getB_name()+"',b_context= '"+boardDTO.getB_context()+"', b_mod_date=NOW() WHERE b_no="+b_no;

        int result=-1;
        try {
            result=jdbcTemplate.update(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public int DeletePost(int b_no) {
        log.info("[BoardDao] DeletePost");
        String sql="DELETE FROM kim_tbl_boardpost WHERE b_no="+b_no;

        int result=-1;
        try {
            result=jdbcTemplate.update(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void addViewNum(int i, int b_no) {
        log.info("[BoardDao] addViewNum");
        String sql="UPDATE kim_tbl_boardpost SET b_viewnum="+i+" WHERE b_no="+b_no;

        int result=-1;
        try {
            result=jdbcTemplate.update(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

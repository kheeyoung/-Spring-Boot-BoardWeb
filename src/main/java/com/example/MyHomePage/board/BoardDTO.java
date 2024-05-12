package com.example.MyHomePage.board;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardDTO {
    int b_no;
    String b_name;
    int b_writer_m_no;
    String b_gift;
    int b_receiver_m_no;
    int b_viewnum;
    String b_context;
    String b_reg_date;
    String b_mod_date;
    String b_writer_m_name;
    String b_receiver_m_name;


}

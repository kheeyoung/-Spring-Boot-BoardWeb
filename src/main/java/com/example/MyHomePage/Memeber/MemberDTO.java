package com.example.MyHomePage.Memeber;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    int m_no;
    int m_isAdmin;
    String m_id;
    String m_pw;
    String m_name;
    String m_gender;
    String m_mail;
    String m_phone;
    String m_reg_date;
    String m_mod_date;
    int m_coin;
}

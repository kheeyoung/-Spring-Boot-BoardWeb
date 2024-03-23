package com.example.MyHomePage.gift;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftService {
    final static public int SUCCESS=1;
    final static public int FAIL=-1;

    @Autowired
    GiftDao GiftDao;
    public List<GiftDTO> getGift() {
        System.out.println("[GiftService] getGift");
        return GiftDao.getGift();
    }

    public int AddGift(GiftDTO giftDTO) {
        System.out.println("[GiftService] AddGift");
        int result = GiftDao.AddGift(giftDTO);
        if (result == 1) { //포스팅 성공시
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public int giftDelete(String g_no) {
        System.out.println("[GiftService] giftDelete");
        int result = GiftDao.giftDelete(g_no);
        if (result == 1) { //삭제 성공시
            return SUCCESS;
        } else {
            return FAIL;
        }
    }
}

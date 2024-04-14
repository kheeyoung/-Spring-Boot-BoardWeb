package com.example.MyHomePage.gift;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GiftService {
    final static public int SUCCESS=1;
    final static public int FAIL=-1;

    @Autowired
    GiftDao GiftDao;

    //전체 선물 가져오기
    public List<GiftDTO> getGift() {
        log.info("[GiftService] getGift");
        return GiftDao.getGift();
    }

    //선물 추가하기
    public int AddGift(GiftDTO giftDTO) {
        log.info("[GiftService] AddGift");
        int result = GiftDao.AddGift(giftDTO);
        if (result == 1) { //포스팅 성공시
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    //선물 삭제
    public int giftDelete(String g_no) {
        log.info("[GiftService] giftDelete");
        int result = GiftDao.giftDelete(g_no);
        if (result == 1) { //삭제 성공시
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    //특정 선물 선물 명으로 가져오기
    public GiftDTO getGift(String bGift) {
        log.info("[GiftService] getGift");
        return GiftDao.getGift(bGift);
    }
}

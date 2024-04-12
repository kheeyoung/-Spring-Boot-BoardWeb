package com.example.MyHomePage.item;

import com.example.MyHomePage.Memeber.MemberService;
import com.example.MyHomePage.myPage.LikePointDTO;
import com.example.MyHomePage.myPage.SpecialGiftDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ItemService {
    @Autowired
    ItemDao ItemDao;
    @Autowired
    MemberService MemberService;
    @Autowired(required=false)
    LikePointDTO LikePointDTO;
    final static public int SUCCESS=1;
    final static public int FAIL=-1;

    public List<ItemDTO> getMyItem(int i_who_Have_m_no) {
        log.info("[ItemService] getMyItem");
        List<ItemDTO> ItemDTOs =ItemDao.getMyItem(i_who_Have_m_no);
        return ItemDTOs;
    }
    public ItemDTO getMyItem(int mNo, String bGift) {
        log.info("[ItemService] getMyItem");
        ItemDTO ItemDTO =ItemDao.getMyItem(mNo,bGift);
        return ItemDTO;
    }

    public int addItem(String winningGift, int i_who_Have_m_no) {
        log.info("[ItemService] addItem");
        int result=ItemDao.addItme(winningGift,i_who_Have_m_no);
        if(result==1){
            return SUCCESS;
        }
        else {
            return FAIL;
        }

    }

    public void useItem(int useItemNo,String b_receiver, int likePoint) {
        log.info("[ItemService] useItem");
        ItemDao.useItem(useItemNo,b_receiver,likePoint);
    }

    public List<LikePointDTO> getLikePoint(int m_no) {
        log.info("[ItemService] getLikePoint");
        List<LikePointDTO> LikePointDTOs=ItemDao.getMyLikePoint(m_no);                //호감도 받아오기

        return LikePointDTOs;
    }

//특별 선물 얻은 거 추가하기
    public void addSpecialItem(String name, int mNo) {
        log.info("[ItemService] addSpecialItem");
        ItemDao.addSpecialItme(name,mNo);
    }


    public int checkHaveSpecialGift(String name, int m_no) {
        log.info("[ItemService] haveSpecialGift");
        int result=ItemDao.checkHaveSpecialGift(name,m_no);
        if(result==1){
            return SUCCESS;
        }
        else {
            return FAIL;
        }
    }

    public List<SpecialGiftDTO> getMySpecialItem(int m_no) {
        log.info("[ItemService] getMySpecialItem");
        return ItemDao.getMySpecialItem(m_no);
    }
}

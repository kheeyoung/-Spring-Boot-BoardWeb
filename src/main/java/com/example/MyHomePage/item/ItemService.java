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

    //특정 멤버가 가진 아이템 가져오기 (m_no로)
    public List<ItemDTO> getMyItem(int i_who_Have_m_no) {
        log.info("[ItemService] getMyItem");
        List<ItemDTO> ItemDTOs =ItemDao.getMyItem(i_who_Have_m_no);
        return ItemDTOs;
    }

    //특정 멤버가 가진 아이템 가져오기 (m_no,i_name로)
    public ItemDTO getMyItem(int mNo, String bGift) {
        log.info("[ItemService] getMyItem");
        ItemDTO ItemDTO =ItemDao.getMyItem(mNo,bGift);
        return ItemDTO;
    }

    //특정 멤버가 가진 아이템 추가
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

    //특정 멤버가 아이템 사용하기
    public void useItem(int useItemNo,String b_receiver_m_name, int likePoint) {
        log.info("[ItemService] useItem");
        //받는 사람 이름으로 번호 가져오기
        int b_receiver_m_no=MemberService.getMemberByName(b_receiver_m_name).getM_no();
        ItemDao.useItem(useItemNo,b_receiver_m_no,likePoint);
    }

    //특정 멤버의 호감도 계산하기
    public List<LikePointDTO> getLikePoint(int m_no) {
        log.info("[ItemService] getLikePoint");
        List<LikePointDTO> LikePointDTOs=ItemDao.getMyLikePoint(m_no);  //호감도 받아오기

        return LikePointDTOs;
    }

    //특정 멤버에게 특별 아이템 가지게 하기
    public void addSpecialItem(String name, int mNo) {
        log.info("[ItemService] addSpecialItem");
        ItemDao.addSpecialItme(name,mNo);
    }

    //특정 멤버가 다른 멤버의 특별 선물 가지고 있나 확인하기
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

    //특정 멤버가 가지고 있는 특별 선물 전부 가져오기
    public List<SpecialGiftDTO> getMySpecialItem(int m_no) {
        log.info("[ItemService] getMySpecialItem");
        return ItemDao.getMySpecialItem(m_no);
    }

}

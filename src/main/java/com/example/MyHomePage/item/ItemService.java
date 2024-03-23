package com.example.MyHomePage.item;

import com.example.MyHomePage.board.BoardDTO;
import com.example.MyHomePage.gift.GiftDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemDao ItemDao;
    final static public int SUCCESS=1;
    final static public int FAIL=-1;

    public List<ItemDTO> getMyItem(int i_who_Have_m_no) {
        System.out.println("[ItemService] getMyItem");
        List<ItemDTO> ItemDTOs =ItemDao.getMyItem(i_who_Have_m_no);
        return ItemDTOs;
    }

    public int addItem(String winningGift, int i_who_Have_m_no) {
        System.out.println("[ItemService] addItem");
        int result=ItemDao.addItme(winningGift,i_who_Have_m_no);
        if(result==1){
            return SUCCESS;
        }
        else {
            return FAIL;
        }

    }
}

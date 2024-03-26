package com.example.MyHomePage.item;

import com.example.MyHomePage.Memeber.MemberService;
import com.example.MyHomePage.likePoint.LikePointDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
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
        List<ItemDTO> ItemDTOs =ItemDao.getMyUsedItem(m_no);                //사용한 적 있는 아이템 받아오기
        HashMap<String, Integer> LikePoint= new HashMap<String, Integer>();
        for (int i=0;i<ItemDTOs.size();i++){                               //아이템 쓴 횟수만큼 반복
            if(ItemDTOs.get(i).getI_get_point()!=0){                        //호감도가 오른 항목이 있다면
                if(LikePoint.containsKey(ItemDTOs.get(i).getI_who_get())){  //현재 해쉬에 추가된 항목이 있나 확인
                    int point=LikePoint.get(ItemDTOs.get(i).getI_who_get());
                    LikePoint.replace(ItemDTOs.get(i).getI_who_get(),point+1);//호감도 값을 +1
                }
                else {                                                      //해쉬 맵에 없다면
                    LikePoint.put(ItemDTOs.get(i).getI_who_get(), 1);       //해쉬 맵에 추가
                }
            }
        }

        List<LikePointDTO> LikePointDTOs=new ArrayList<LikePointDTO>();     //호감도 생성자 담을 배열 선언
        List<String> Membernames=new ArrayList<String>();                   //멤버 이름 담는 리스트
        Membernames=MemberService.getMemberId();                            //멤버 이름 받아옴
        for(int i=0;i<Membernames.size();i++){
            LikePointDTO LikePointDTO=new LikePointDTO();                   //호감도 DTO 선언
            LikePointDTO.setName(Membernames.get(i));                       //해시맵에서 받아서 이름 세팅하고
            if(LikePoint.containsKey(Membernames.get(i))) {                 //해당 멤버에게 호감도가 있다면
                LikePointDTO.setPoint(LikePoint.get(Membernames.get(i)));   //해시멤에서 받아서 호감도 설정
            }
            else{
                LikePointDTO.setPoint(0);                                   //호감도 없으면 (해시맵에서 못 찾으면) 0으로 설정
            }
            LikePointDTOs.add(LikePointDTO);
        }

        return LikePointDTOs;
    }
}

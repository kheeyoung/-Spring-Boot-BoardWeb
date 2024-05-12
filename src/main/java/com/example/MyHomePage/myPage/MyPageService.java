package com.example.MyHomePage.myPage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MyPageService {
    @Autowired
    MyPageDao MyPageDao;

    //등록된 선물이 있나 확인
    public boolean checkSomeonesSpecialGift(int no) {
        log.info("[MyPageService] checkSomeonesSpecialGift");

        return MyPageDao.checkSomeonesSpecialGift(no);
    }
    public boolean checkSomeonesSpecialGift(String name) {
        log.info("[MyPageService] checkSomeonesSpecialGift");

        return MyPageDao.checkSomeonesSpecialGift(name+"의 특별 선물");
    }

    //특별 선물 등록
    public void saveSpecialGift(SpecialGiftDTO specialGiftDTO) {
        log.info("[MyPageService] saveSpecialGift");
        MyPageDao.saveSpecialGift(specialGiftDTO);

    }

    //특별 선물 얻기
    public List<SpecialGiftDTO> getSpecialGift() {
        log.info("[MyPageService] getSpecialGift");
        return MyPageDao.getSpecialGift();
    }

    //특별 선물 삭제
    public void deleteSpecialGift(int sg_no) {
        log.info("[MyPageService] deleteSpecialGift");
        MyPageDao.deleteSpecialGift(sg_no);
    }

    //특별 선물 수정
    public void editSpecialGift(SpecialGiftDTO specialGiftDTO) {
        log.info("[MyPageService] editSpecialGift");
        MyPageDao.editSpecialGift(specialGiftDTO);
    }
}

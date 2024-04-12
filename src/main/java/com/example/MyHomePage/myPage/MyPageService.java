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

    public boolean checkSomeonesSpecialGift(String name) {
        log.info("[MyPageService] checkSomeonesSpecialGift");

        return MyPageDao.checkSomeonesSpecialGift(name);
    }

    public void saveSpecialGift(SpecialGiftDTO specialGiftDTO) {
        log.info("[MyPageService] saveSpecialGift");

        MyPageDao.saveSpecialGift(specialGiftDTO);

    }

    public List<SpecialGiftDTO> getSpecialGift() {
        log.info("[MyPageService] getSpecialGift");
        return MyPageDao.getSpecialGift();
    }
}

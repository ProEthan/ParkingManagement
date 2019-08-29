package com.example.parking.service;

/**
 * 中控 service
 */
public class UserGarageService {

    /**
     * 大致流程
     *
     * @param cardId 车牌号码
     */
    public void controller(String cardId) {
        /*
            1. check是进还是出
            2. if(出)
                 if(is_month_rent)
                    return
                 else
                    算钱
               else
                 if(!is_register)
                     注册
                 else if(is_month_rent)
                    进
                 选择月租 or 单次
                 进
         */
    }
}

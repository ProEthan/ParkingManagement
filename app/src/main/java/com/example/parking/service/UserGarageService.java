package com.example.parking.service;

import com.example.parking.bean.User;
import com.example.parking.dao.UserDaoImpl;

/**
 * 中控 service
 */
public class UserGarageService {

    private static UserDaoImpl userDao = new UserDaoImpl();

    /**
     * 大致流程
     *
     * @param cardId 车牌号码
     */
    public void controller(String cardId) {
        //1、查询用户是否注册

        //2、如果没注册，跳到注册页面，走入库流程

        //3、如果注册，判断该车是入库还是出库，根据cardId

        //4、如果入库，进入入库流程

        //5、如果出库，进入出库流程
    }

    /**
     * 注册用户。
     *
     * @param number      车牌号
     * @param username    username
     * @param isMonthRent 是否月租(1,0)
     */
    public void register(String number, String username, byte isMonthRent) {
        User user = new User();
        user.setNumber(number);
        user.setUsername(username);
        user.setIsMonthRent(isMonthRent);

        if (isMonthRent > 0) {
            user.setMonthRentStartTime(System.currentTimeMillis());
        }

        userDao.save(user);
    }

    /**
     * @param number 车牌号
     */
    public User getByNumber(String number) {
        return userDao.queryByNumber(number);
    }
}

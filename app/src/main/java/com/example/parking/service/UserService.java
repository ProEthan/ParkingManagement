package com.example.parking.service;

import com.example.parking.bean.User;
import com.example.parking.dao.UserDaoImpl;

public class UserService {

    private static UserDaoImpl userDao = new UserDaoImpl();

    /**
     * 注册用户。
     *
     * @param number      车牌号
     * @param username    username
     * @param isMonthRent 是否月租(1,0)
     */
    public void register(String number, String username, boolean isMonthRent) {
        User user = new User();
        user.setNumber(number);
        user.setUsername(username);
        user.setIsMonthRent(isMonthRent);

        if (isMonthRent) {
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

    public boolean deleteByNumber(String number) {
        return userDao.deleteByNumber(number);
    }
}

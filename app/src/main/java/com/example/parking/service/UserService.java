package com.example.parking.service;

import com.example.parking.bean.User;
import com.example.parking.dao.UserDaoImpl;

public class UserService {

    private static UserDaoImpl userDao = new UserDaoImpl();

    /**
     * @param number plate number
     */
    public User getByNumber(String number) {
        return userDao.queryByNumber(number);
    }

    /**
     * 注册或更新用户信息。
     *
     * @param number      plate number
     * @param username    the specified username
     * @param isMonthRent 是否月租(1,0)
     * @return true or false
     */
    public boolean saveOrUpdate(String number, String username, boolean isMonthRent) {
        User user = new User();
        user.setNumber(number);
        user.setUsername(username);
        user.setMonthRent(isMonthRent);
        if (isMonthRent) {
            user.setMonthRentStartTime(System.currentTimeMillis());
        }
        return userDao.saveOrUpdate(number, user);
    }

    public int monthRentExpired(String number, String username) {
        User user = new User();
        user.setNumber(number);
        user.setUsername(username);
        user.setMonthRent(false);
        return userDao.updateMonthRentByNumber(number, user);
    }

    public boolean deleteByNumber(String number) {
        return userDao.deleteByNumber(number);
    }
}

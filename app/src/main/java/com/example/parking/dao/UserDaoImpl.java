package com.example.parking.dao;


import com.example.parking.bean.User;

import org.litepal.LitePal;

public class UserDaoImpl {
    public boolean save(User user) {
        return user.save();
    }

    public User queryByNumber(String number) {
        return LitePal.where("number = ?", number).findFirst(User.class);

    }

    public boolean saveOrUpdate(String number, User user) {
        return user.saveOrUpdate("number = ?", number);
    }

    public int updateMonthRentByNumber(String number, User user) {
        return user.updateAll("number = ?", number);
    }

    public boolean deleteByNumber(String number) {
        int i = LitePal.deleteAll(User.class, "number = ?", number);
        return i > 0;
    }
}
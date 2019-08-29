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

    public boolean deleteByNumber(String number) {
        int i = LitePal.deleteAll(User.class, "number = ?", number);
        return i > 0;
    }
}
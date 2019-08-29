package com.example.parking.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    @Column(nullable = false)
    private String number;

    private String username;

    @Column(nullable = false)
    private byte isMonthRent;

    private long monthRentStartTime;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte getIsMonthRent() {
        return isMonthRent;
    }

    public void setIsMonthRent(byte isMonthRent) {
        this.isMonthRent = isMonthRent;
    }

    public long getMonthRentStartTime() {
        return monthRentStartTime;
    }

    public void setMonthRentStartTime(long monthRentStartTime) {
        this.monthRentStartTime = monthRentStartTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "number='" + number + '\'' +
                ", username='" + username + '\'' +
                ", isMonthRent=" + isMonthRent +
                ", monthRentStartTime=" + monthRentStartTime +
                '}';
    }
}

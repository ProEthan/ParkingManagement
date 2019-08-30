package com.example.parking.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 车库关系表
 */
public class GarageRelation extends LitePalSupport {

    private Integer garageId;//车库号

    private String carId;//车牌号

    private Long entryTime;//入库时间

    private Boolean isRent;//是否月租

    public Integer getGarageId() {
        return garageId;
    }

    public void setGarageId(Integer garageId) {
        this.garageId = garageId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public Long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Long entryTime) {
        this.entryTime = entryTime;
    }

    public Boolean getRent() {
        return isRent;
    }

    public void setRent(Boolean rent) {
        isRent = rent;
    }

    @Override
    public String toString() {
        return "GarageRelation{" +
                "garageId=" + garageId +
                ", carId='" + carId + '\'' +
                ", entryTime=" + entryTime +
                ", isRent=" + isRent +
                '}';
    }
}

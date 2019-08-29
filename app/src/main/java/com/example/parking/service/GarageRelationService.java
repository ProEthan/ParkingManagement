package com.example.parking.service;

import com.example.parking.bean.GarageRelation;
import com.example.parking.dao.GarageRelationDao;

import java.util.HashSet;
import java.util.Set;



// GarageRelation service
public class GarageRelationService {

    private static GarageRelationDao garageRelationDao = new GarageRelationDao();

    //获取所有在使用的车库号
    public Set<Integer> getAllGarageId(){
        return new HashSet<Integer>(garageRelationDao.getAllGarageId());
    }

    //根据carId、isRent、garageId 添加GarageRelation对象
    public boolean addGarageRelation(String cardId , boolean isRent , int garagedId){
        GarageRelation garageRelation = new GarageRelation();
        garageRelation.setCarId(cardId);
        garageRelation.setRent(isRent);
        garageRelation.setGarageId(garagedId);
        garageRelation.setEntryTime(System.currentTimeMillis() / 1000); //秒级别的
        return garageRelationDao.addGarageRelation(garageRelation);
    }

    //根据cardId 删除GarageRelation对象
    public int deleteGarageRelation(String carId){
        return garageRelationDao.deleteGarageRelation(carId);
    }

    //根据cardId 获取GarageRelation对象
    public GarageRelation getGarageRelation(String cardId){
        return garageRelationDao.getGarageRelation(cardId);
    }
}

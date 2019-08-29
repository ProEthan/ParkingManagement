package com.example.parking.dao;

import com.example.parking.bean.GarageRelation;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class GarageRelationDao {

    //获取所有的车库号
    public List<Integer> getAllGarageId(){
        List<Integer> res = new ArrayList<>();
        List<GarageRelation> list = LitePal.select("garageId").find(GarageRelation.class);
        for(int i = 0 ; i < list.size() ; i++){
            res.add(list.get(i).getGarageId());
        }
        return res;
    }

    //添加garageRelation 对象
    public boolean addGarageRelation(GarageRelation garageRelation){
        return garageRelation.save();
    }

    //根据车牌号出库
    public int deleteGarageRelation(String carId){
        return LitePal.deleteAll(GarageRelation.class , "carId = ?" , carId);
    }

    //根据cardId 获取garageRelation 对象
    public GarageRelation getGarageRelation(String cardId){
        List<GarageRelation> res = LitePal.where("carId = ?" , cardId).find(GarageRelation.class);
        return res.size() == 0 ? null : res.get(0);
    }

}

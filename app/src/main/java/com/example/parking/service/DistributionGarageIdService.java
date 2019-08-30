package com.example.parking.service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

//分发车库号
public class DistributionGarageIdService {

    private static GarageRelationService garageRelationService = new GarageRelationService();

    //默认为200个车库号，从1 ~ 200
    private static final int garageIdSize = 200;

    //已经使用过的GarageId
    private static Set<Integer> useedGarageId = null;
    //还未使用过的GarageId
    private static Set<Integer> unUseedGarageId = null;

    //初始化Set集合
    private static void init(){
        Set<Integer> set1 = garageRelationService.getAllGarageId();
        Set<Integer> set2 = new HashSet<>();
        for(int i = 1 ; i <= garageIdSize ; i++){
            if(!set1.contains(i)){
                set2.add(i);
            }
        }
        useedGarageId = set1;
        unUseedGarageId = set2;
    }

    //入库，获取车牌号
    public int getGarageId(){
        if(useedGarageId == null || unUseedGarageId == null){
            init();
        }
        if(unUseedGarageId.isEmpty()){
            return -1;
        }
        Object[] objs = unUseedGarageId.toArray();
        int ran = new Random().nextInt(objs.length);
        unUseedGarageId.remove(objs[ran]);
        useedGarageId.add((Integer) objs[ran]);
        return (Integer)objs[ran];
    }

    //出库
    public void outGarageId(int garageId){
        if(useedGarageId == null || unUseedGarageId == null){
            init();
        }
        useedGarageId.remove(garageId);
        unUseedGarageId.add(garageId);
    }

    //剩余车库个数
    public int leaveGaragedIdNubers(){
        if(useedGarageId == null || unUseedGarageId == null){
            init();
        }
        return unUseedGarageId.size();
    }

    //已使用车库个数
    public int useedGaragedIdNumbers(){
        if(useedGarageId == null || unUseedGarageId == null){
            init();
        }
        return useedGarageId.size();
    }
}

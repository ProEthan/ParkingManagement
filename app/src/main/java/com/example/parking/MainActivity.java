package com.example.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parking.service.GarageRelationService;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static GarageRelationService garageRelationService = new GarageRelationService();

    private int ParkingNumber=200; //车库可用的车位
    private TextView ParkingNum;
    private Button ScanButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.initialize(this);
        setContentView(R.layout.activity_main);
        ParkingNum=findViewById(R.id.ParkingNum);
        ScanButton=findViewById(R.id.ScanButton);
        ParkingNum.setText(ParkingNumber+"");
        ScanButton.setOnClickListener(this);

        //测试关联表数据。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
        System.out.println(".......................");
        garageRelationService.addGarageRelation("A123456" , false , 34);
        System.out.println(".......................");
        garageRelationService.addGarageRelation("A889996" , true , 19);
        System.out.println(".......................");
        garageRelationService.addGarageRelation("A666688" , false , 28);

        System.out.println(garageRelationService.getAllGarageId());

        System.out.println(garageRelationService.getGarageRelation("A123456"));

        garageRelationService.deleteGarageRelation("A123456");

        System.out.println(garageRelationService.getGarageRelation("A889996"));
        System.out.println(garageRelationService.getAllGarageId());

        garageRelationService.deleteGarageRelation("A889996");
        garageRelationService.deleteGarageRelation("A666688");

        System.out.println(garageRelationService.getAllGarageId());
        System.out.println(garageRelationService.getGarageRelation("A666688"));

        //测试关联表数据。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
    }

    //调用扫描识别车牌
    @Override
    public void onClick(View v) {
        String CarNum = Scan();
        if (Judge(CarNum) == 1) {
            Intent intent = new Intent(MainActivity.this, EnterActivity.class);
            intent.putExtra("CarNum", CarNum);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, LeaveActivity.class);
            intent.putExtra("CarNum", CarNum);
            startActivity(intent);

        }
    }

    //扫描函数返回车牌号码
    private String Scan () {
        String CarNum = "1233";
        return CarNum;
    }

    //判断车牌号码是否在数据库内，如果在数据库内返回1，不在返回0
    //judge函数为0跳转到enter页面，为1跳转leave页面
    private int Judge (String CarNum){
        return 1;
    }






}


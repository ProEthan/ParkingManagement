package com.example.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parking.service.GarageRelationService;
import com.example.parking.service.UserService;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static GarageRelationService garageRelationService = new GarageRelationService();
    private static UserService userService = new UserService();

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


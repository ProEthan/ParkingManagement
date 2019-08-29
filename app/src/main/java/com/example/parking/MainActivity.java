package com.example.parking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parking.bean.GarageRelation;
import com.example.parking.bean.User;
import com.example.parking.service.GarageRelationService;
import com.example.parking.service.UserService;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static GarageRelationService garageRelationService = new GarageRelationService();
    private static UserService userService = new UserService();

    private int ParkingNumber = 0; //车库可用的车位
    private TextView ParkingNum;
    private Button ScanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.initialize(this);
        setContentView(R.layout.activity_main);
        ParkingNum = findViewById(R.id.ParkingNum);
        ScanButton = findViewById(R.id.ScanButton);
        ParkingNum.setText(ParkingNumber + "");
        ScanButton.setOnClickListener(this);
    }

    //调用扫描识别车牌
    @Override
    public void onClick(View v) {
        if (ParkingNumber <= 0) {
            Toast.makeText(MainActivity.this, "暂无车位！" , Toast.LENGTH_SHORT).show();
            return;
        }

        String plateNumber = Scan();

        GarageRelation garageRelation = garageRelationService.getGarageRelation(plateNumber);
        // 出
        if (garageRelation != null) {
            if (garageRelation.getRent()) {
                // 是月租
            } else {
                // 计算费用
                long time = System.currentTimeMillis() - garageRelation.getEntryTime();
                long cost = time * 1000 * 60 * 60 * 5;
            }
        }else { // 停车
            User user = userService.getByNumber(plateNumber);
            if (null == user) {
                // 注册流程
                Intent intent = new Intent(MainActivity.this, EnterActivity.class);
                intent.putExtra("CarNum", plateNumber);
                startActivity(intent);
            } else if (user.getMonthRent()) {
                // 直接进入
                Intent intent = new Intent(MainActivity.this, LeaveActivity.class);
                intent.putExtra("CarNum", plateNumber);
                startActivity(intent);
            }

        }
    }

    //扫描函数返回车牌号码
    private String Scan() {
        String CarNum = "苏A12345";
        return CarNum;
    }

}


package com.example.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.parking.bean.User;
import com.example.parking.service.GarageRelationService;
import com.example.parking.service.UserService;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {
    private String CarNum;      //车牌
    private String carUser;     //车主
    private Button MonthBt;
    private Button HourBt;

    private UserService userService = new UserService();
    private static GarageRelationService garageRelationService = new GarageRelationService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        MonthBt = findViewById(R.id.Month);
        HourBt = findViewById(R.id.Hour);
        CarNum = getIntent().getStringExtra("CarNum");
        carUser = getIntent().getStringExtra("carUser");

        MonthBt.setOnClickListener(this);
        HourBt.setOnClickListener(this);
    }

    //点击包月或者按次计费之后跳到主页
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Month:
                registerAndEnter(true);
                Intent intent1 = new Intent(ChooseActivity.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.Hour:
                registerAndEnter(false);
                Intent intent2 = new Intent(ChooseActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void registerAndEnter(boolean isMonthRent) {
        User user = new User();
        user.setNumber(CarNum);
        user.setUsername(carUser);
        user.setMonthRent(isMonthRent);
        user.setMonthRentStartTime(System.currentTimeMillis());
        userService.saveOrUpdate(CarNum, user);

        // TODO
        garageRelationService.addGarageRelation(CarNum, isMonthRent, 1);
    }
}
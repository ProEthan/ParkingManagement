package com.example.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {
    private String CarNum;      //车牌
    private Button MonthBt;
    private Button HourBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        MonthBt=findViewById(R.id.Month);
        HourBt=findViewById(R.id.Hour);
        CarNum=getIntent().getStringExtra("CarNum");

        MonthBt.setOnClickListener(this);
        HourBt.setOnClickListener(this);
    }

    //点击包月或者按次计费之后跳到主页
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Month:
                Intent intent1 = new Intent(ChooseActivity.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.Hour:
                Intent intent2 = new Intent(ChooseActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
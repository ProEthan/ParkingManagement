package com.example.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LeaveActivity extends AppCompatActivity implements View.OnClickListener {
    private String CarNum;      //车牌
    private String CarUser;     //车主
    private String PlaceNum;    //车库号
    private String Time;        //总时间
    private int Cost;           //费用
    private Button LeaveBt;
    private TextView CarNumTextView;
    private TextView CarUserTextView;
    private TextView PlaceNumTextView;
    private TextView TimeTextView;
    private TextView CostTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave);

        PlaceNum = "123"; // 测试用
        Cost = 333;

        CarNumTextView=findViewById(R.id.CarNum);
        CarUserTextView=findViewById(R.id.CarUser);
        PlaceNumTextView=findViewById(R.id.PlaceNum);
        TimeTextView=findViewById(R.id.Time);
        CostTextView=findViewById(R.id.Cost);
        LeaveBt=findViewById(R.id.Leave);

        CarNum=getIntent().getStringExtra("CarNum");
        CarNumTextView.setText(CarNum);

        Change();
        CarNumTextView.setText(CarNum);
        CarUserTextView.setText(CarUser);
        PlaceNumTextView.setText("123");
        TimeTextView.setText(Time);
        CostTextView.setText(Cost+"");

        LeaveBt.setOnClickListener(this);
    }


    //根据扫描后的车牌号，在数据库找出数据并且给CarUser,PalaceNum,Time,Cost计算并且赋值
    private void Change(){

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LeaveActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
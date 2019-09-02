package com.example.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parking.bean.User;
import com.example.parking.dao.GarageRelationDao;
import com.example.parking.service.DistributionGarageIdService;

public class LeaveActivity extends AppCompatActivity implements View.OnClickListener {
    private String CarNum;      //车牌
    private String CarUser;     //车主
    private int PlaceNum;    //车库号
    private long time;        //总时间
    private long cost;           //费用
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

        CarNumTextView = findViewById(R.id.CarNum);
        CarUserTextView = findViewById(R.id.CarUser);
        PlaceNumTextView = findViewById(R.id.PlaceNum);
        TimeTextView = findViewById(R.id.Time);
        CostTextView = findViewById(R.id.Cost);
        LeaveBt = findViewById(R.id.Leave);

        PlaceNum = getIntent().getIntExtra("garageId", 0);
        time = getIntent().getLongExtra("time", 0L);
        cost = getIntent().getLongExtra("cost", 0L);
        User user = (User) getIntent().getSerializableExtra("user");

        CarNumTextView.setText(user.getNumber());
        CarUserTextView.setText(user.getUsername());


        leave(user.getNumber());
        // CarNumTextView.setText(CarNum);
        // CarUserTextView.setText(CarUser);
        PlaceNumTextView.setText(PlaceNum + "");
        TimeTextView.setText(time + "小时");
        CostTextView.setText(cost + "元");

        LeaveBt.setOnClickListener(this);
    }

    // 出库 删除数据库相关信息
    private void leave(String CarNum) {
        GarageRelationDao garageRelationDao = new GarageRelationDao();
        DistributionGarageIdService distributionGarageIdService = new DistributionGarageIdService();
        //删除关联表相关信息
        garageRelationDao.deleteGarageRelation(CarNum);
        //维护set集合
        distributionGarageIdService.outGarageId(PlaceNum);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LeaveActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
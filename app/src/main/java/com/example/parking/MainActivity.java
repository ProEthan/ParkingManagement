package com.example.parking;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.parking.bean.GarageRelation;
import com.example.parking.bean.User;
import com.example.parking.service.GarageRelationService;
import com.example.parking.service.UserService;
import com.example.parking.util.GetCarCode;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static GarageRelationService garageRelationService = new GarageRelationService();
    private static UserService userService = new UserService();

    private int ParkingNumber = 200; //车库可用的车位
    private TextView ParkingNum;
    private Button ScanButton;
    private Button choosePhoto;
    private ImageView picture;

    public static final int TAKE_PHOTO = 1; // 拍照
    public static final int CHOOSE_PHOTO = 2; // 选择相册
    private static String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Uri imageUri;
    private static String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.initialize(this);
        setContentView(R.layout.activity_main);
        ParkingNum = findViewById(R.id.ParkingNum);
        ScanButton = findViewById(R.id.ScanButton);
        choosePhoto = findViewById(R.id.choose_from_album);
        picture = findViewById(R.id.iv_picture);
        ParkingNum.setText(ParkingNumber + "");
        ScanButton.setOnClickListener(this);
        choosePhoto.setOnClickListener(this);
        getPermission();
    }

    //调用扫描识别车牌
    @Override
    public void onClick(View v) {
        if (ParkingNumber <= 0) {
            Toast.makeText(MainActivity.this, "暂无车位！", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.choose_from_album:
                getPermission();
                openAlbum();
                break;
            case R.id.ScanButton:
                scan();
        }
    }

    //打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    //扫描函数返回车牌号码
    private void scan() {
        getPermission();

        //创建file对象，用于存储拍照后的图片；
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(MainActivity.this,
                    "com.example.parking.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 拍照
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bm);

                        String img = getExternalCacheDir().getCanonicalPath() + "/output_image.jpg";
//                        System.out.println(GetCarCode.checkFile(img));
                        url = img;
                        new Thread(networkTask).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            // 选择图片
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    Uri uri = data.getData();
                    System.out.println(uri.toString());
                    String pathOfPicture = getAbsoluteImagePath(uri);
                    url = pathOfPicture;
                    System.out.println("ok============================="
                            + pathOfPicture);
                    Log.e("uri", uri.getHost());
                    ContentResolver cr = this.getContentResolver();
                    InputStream is = null;
                    try {
                        is = cr.openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    bitmap = BitmapFactory.decodeStream(is);
                    picture.setImageBitmap(bitmap);

                    if (pathOfPicture != null) {
                        new Thread(networkTask).start();
                    }
                }
            default:
                break;
        }
    }

    protected String getAbsoluteImagePath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = this.getContentResolver().query(uri, proj, null, null,
                null);

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
//            Message msg = new Message();
//            Bundle data = new Bundle();

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            try {
                String result = GetCarCode.checkFile(url);
                System.out.println(result);
                JSONObject jsonObject = JSONObject.parseObject(result);

                if (jsonObject.getString("error_code") != null) {
                    System.out.println(result);
                    Toast.makeText(MainActivity.this, "识别失败，请重试！", Toast.LENGTH_SHORT).show();
                } else {
                    String plateNumber = jsonObject.getJSONObject("words_result").getString("number");

                    System.out.println(plateNumber);
                    park(plateNumber);

//                    System.out.println("Plate Number : " + plateNumber);
//                    data.putString("value", result);
//                    msg.setData(data);
//                    handler.sendMessage(msg);
                }
                Looper.loop();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    };

    private void park(String plateNumber) {
        GarageRelation garageRelation = garageRelationService.getGarageRelation(plateNumber);
        // 出
        if (garageRelation != null) {
            User user = userService.getByNumber(plateNumber);
            long time = (System.currentTimeMillis() / 1000 - garageRelation.getEntryTime()) / 60 / 60 + 1;
            Intent intent = new Intent(MainActivity.this, LeaveActivity.class);
            if (garageRelation.getRent()) {
                // 是月租
                intent.putExtra("cost", 0L);
            } else {
                intent.putExtra("time", time);
            }
            // 小时向上取整
            long cost = time * 5;
            // Toast.makeText(MainActivity.this, "您需要支付 " + cost + "元", Toast.LENGTH_SHORT).show();
            intent.putExtra("cost", cost);
            intent.putExtra("garageId", garageRelation.getGarageId());
            intent.putExtra("user", user);
            startActivity(intent);
            // 出库
            // TODO
            garageRelationService.deleteGarageRelation(garageRelation.getCarId());
        } else { // 停车
            User user = userService.getByNumber(plateNumber);
            if (null == user) {
                // 注册流程
                Intent intent = new Intent(MainActivity.this, EnterActivity.class);
                intent.putExtra("CarNum", plateNumber);
                startActivity(intent);
                return;
            } else if (user.getMonthRent()) {
                // 直接进入
                // TODO
                garageRelationService.addGarageRelation(plateNumber, true, 1);
                Toast.makeText(MainActivity.this, "欢迎光临！", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
            intent.putExtra("CarNum", plateNumber);
            intent.putExtra("carUser", user.getUsername());
            startActivity(intent);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
    }
}


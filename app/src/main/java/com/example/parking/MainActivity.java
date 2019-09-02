package com.example.parking;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.parking.bean.GarageRelation;
import com.example.parking.bean.User;
import com.example.parking.service.DistributionGarageIdService;
import com.example.parking.service.GarageRelationService;
import com.example.parking.service.UserService;
import com.example.parking.util.GetCarCode;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
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
    private static DistributionGarageIdService distributionGarageIdService = new DistributionGarageIdService();

    private int ParkingNumber; //车库可用的车位
    private TextView ParkingNum;
    private Button ScanButton;
    private Button choosePhoto;
    private ImageView picture;

    public static final int TAKE_PHOTO = 1; // 拍照
    public static final int CHOOSE_PHOTO = 2; // 选择相册
    private static String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Uri imageUri;

    //............byte array
    private static byte[] imagedata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.initialize(this);
        setContentView(R.layout.activity_main);
        ParkingNum = findViewById(R.id.ParkingNum);
        ScanButton = findViewById(R.id.ScanButton);
        choosePhoto = findViewById(R.id.choose_from_album);
        picture = findViewById(R.id.iv_picture);
        ParkingNumber = new DistributionGarageIdService().leaveGaragedIdNubers();
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
            // Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
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

                        if (null != bm) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imageZoom(bm);
                            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);    //注意压缩png和jpg的格式和质量
                            imagedata = baos.toByteArray();
                        }
                        String img = getExternalCacheDir().getCanonicalPath() + "/output_image.jpg";
//                        System.out.println(GetCarCode.checkFile(img));
                        new Thread(networkTask).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            // 选择图片
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap;
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    InputStream is = null;
                    try {
                        is = cr.openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap = BitmapFactory.decodeStream(is);
                    picture.setImageBitmap(bitmap);

                    if (null != bitmap) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);    //注意压缩png和jpg的格式和质量
                        imagedata = baos.toByteArray();
                    }
                    if (imagedata != null) {
                        new Thread(networkTask).start();
                    }
                }
            default:
                break;
        }
    }

    private void imageZoom(Bitmap bitMap) {
        // 图片允许最大空间 单位：KB
        double maxSize = 2000.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
        }
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // 子线程使用Toast
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            try {
                String result = GetCarCode.checkFile(imagedata);
                JSONObject jsonObject = JSONObject.parseObject(result);

                if (jsonObject.getString("error_code") != null) {
                    System.out.println(result);
                    Toast.makeText(MainActivity.this, "识别失败，请重试！", Toast.LENGTH_SHORT).show();
                } else {
                    String plateNumber = jsonObject.getJSONObject("words_result").getString("number");
                    park(plateNumber);
                }
                Looper.loop();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            } finally {
                imagedata = null;
            }
        }
    };

    private void park(String plateNumber) {
        GarageRelation garageRelation = garageRelationService.getGarageRelation(plateNumber);
        // 离开停车场
        if (garageRelation != null) {
            User user = userService.getByNumber(plateNumber);
            // 小时向上取整
            long time = (System.currentTimeMillis() / 1000 - garageRelation.getEntryTime()) / 60 / 60 + 1;
            Intent intent = new Intent(MainActivity.this, LeaveActivity.class);

            // 缴费金额
            long cost = time * 5;
            // 传递相关数据
            intent.putExtra("cost", cost);
            intent.putExtra("time", time);
            intent.putExtra("garageId", garageRelation.getGarageId());
            intent.putExtra("user", user);
            if (garageRelation.getRent()) {
                // 是月租
                intent.putExtra("cost", 0L);
            }
            startActivity(intent);

            // 出库
            //删除关联表信息
            garageRelationService.deleteGarageRelation(garageRelation.getCarId());
            //维护set集合信息
            distributionGarageIdService.outGarageId(garageRelation.getGarageId());

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
                //添加关联表信息
                garageRelationService.addGarageRelation(plateNumber, true, distributionGarageIdService.getGarageId());
                Toast.makeText(MainActivity.this, "欢迎光临！", Toast.LENGTH_SHORT).show();
                return;
            }

            // 进入ChooseActivity
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


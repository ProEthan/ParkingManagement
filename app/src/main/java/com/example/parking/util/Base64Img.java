package com.example.parking.util;


import android.util.Base64;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

public class Base64Img {

    /**
     * 将一张本地图片转化成Base64字符串
     */
    public static String getImageStrFromPath(String imgPath) {
        InputStream in;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getImageStrFromPath(data);
    }

    /**
     *
     * @param data 图像字节数据
     * @return
     */
    public static String getImageStrFromPath(byte[] data){
        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过再URLEncode的字节数组字符串
//        return URLEncoder.encode(encoder.encode(data));
        return URLEncoder.encode(Base64.encodeToString(data, Base64.DEFAULT));
    }
}

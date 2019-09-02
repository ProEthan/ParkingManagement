package com.example.parking.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 获取车牌号码
 */
public class GetCarCode {

    private static final String ACCESS_TOKEN = "24.ebc4ec177b342fbf657635e94d6a8dfa.2592000.1569507323.282335-17116444";
    private static final String POST_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate?access_token="+ACCESS_TOKEN;

    /**
     * 识别本地图片的文字
     */
    public static String checkFile(String path) throws URISyntaxException, IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new NullPointerException("图片不存在");
        }
        String image = Base64Img.getImageStrFromPath(path);
        String param = "image=" + image;
        return post(param);
    }

    /**
     * 识别本地图片的文字
     */
    public static String checkFile(byte[] data) throws URISyntaxException, IOException {
        String image = Base64Img.getImageStrFromPath(data);
        String param = "image=" + image;
        return post(param);
    }

    /**
     * 通过传递参数：image进行文字识别
     */
    public static String post(String parma){
        String authHost = POST_URL;
        try {
            URL realUrl = new URL(authHost);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte[] data = parma.getBytes();
            connection.getOutputStream().write(data , 0 , data.length);
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return null;
    }


}

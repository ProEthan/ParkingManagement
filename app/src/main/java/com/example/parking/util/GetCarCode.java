package com.example.parking.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
    private static String post(String param) throws URISyntaxException, IOException {
        //开始搭建post请求
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost();
        URI url = new URI(POST_URL);
        post.setURI(url);
        //设置请求头，请求头必须为application/x-www-form-urlencoded，因为是传递一个很长的字符串，不能分段发送
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        StringEntity entity = new StringEntity(param);
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String str;
            try {
                //读取服务器返回过来的json字符串数据
                str = EntityUtils.toString(response.getEntity());
                return str;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
//        String path = "H:/ideaCode/CarCodeIdentification/src/main/resources/data/3.png";
//        System.out.println(checkFile(path));
    }

}

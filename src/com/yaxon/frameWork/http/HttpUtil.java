package com.yaxon.frameWork.http;

import android.graphics.Bitmap;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public final class HttpUtil {

    private static final String tag = "HttpUtil";

    public static String postRequest(final String url, final Map<String, String> rawParams) throws Exception, ExecutionException {
        FutureTask<String> task = new FutureTask<>(new Callable<String>() {
            HttpClient httpClient = new DefaultHttpClient();

            @Override
            public String call() throws Exception {
                // TODO Auto-generated method stub

                HttpPost httpPost = new HttpPost(url);
                List<NameValuePair> params = new ArrayList<>();
                for (String key : rawParams.keySet()) {
                    params.add(new BasicNameValuePair(key, rawParams.get(key)));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
//						String result = EntityUtils.toString(httpResponse.getEntity());
//						return result;
                    return EntityUtils.toString(httpResponse.getEntity());
                }
                return null;
            }
        });
        new Thread(task).start();
        return task.get();
    }

    public static String doPost(JSONObject jsonObject, String url) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        // 新建HttpPost对象
        HttpPost resqust = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");// 绑定到请求 Entry
        resqust.setEntity(stringEntity);
        resqust.addHeader("Accept", "application/json");
        HttpResponse httpResponse = httpClient.execute(resqust);// 发送请求
//		String retString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");// 得到应答的字符串，这也是一个 JSON 格式保存的数据
//		return retString;
        return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
    }

    public static String doGet(String url) {
        String result = null;
        // 新建HttpPost对象
        HttpGet httpGet = new HttpGet(url);
        // 获取HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        // 连接超时
        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        // 请求超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                30000);
        try {
            // 获取HttpResponse实例
            HttpResponse httpResp = httpClient.execute(httpGet);
            // 判断是够请求成功
            if (httpResp.getStatusLine().getStatusCode() == 200) {
                // 获取返回的数据
                result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result != null ? result.trim() : null;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}


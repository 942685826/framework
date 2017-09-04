package com.yaxon.frameWork.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * @author guojiaping
 * @version 2015/5/28 创建<br>
 */
public class HttpConnectionUtils implements Runnable {
    public static final int DID_START = 0;
    public static final int DID_ERROR = 1;
    public static final int DID_SUCCEED = 2;

    private static final int GET = 0;
    private static final int POST = 1;
    private static final int PUT = 2;
    private static final int DELETE = 3;
    private static final int BITMAP = 4;

    private String url;
    private String token;
    private int method;
    private Handler handler;
    private HttpEntity entity;


    public static Context context;

    private int number;

    private static final int BUFFER_SIZE = 1024;

    public HttpConnectionUtils() {
        this(new Handler());
    }

    public HttpConnectionUtils(Handler _handler) {
        handler = _handler;

    }

    public HttpConnectionUtils(Handler _handler, int num, Context contexts) {
        handler = _handler;
        number = num;
        context = contexts;
    }

    public void create(int method, String url, HttpEntity entity, String token) {
        this.method = method;
        this.url = url;
        this.entity = entity;
        this.token = token;

        ConnectionManager.getInstance().push(this);
    }

    public void get(String url, String token) {
        create(GET, url, null, token);
    }

    public void post(String url, HttpEntity entity, String token) {
        create(POST, url, entity, token);
    }

    public void put(String url, HttpEntity entity, String token) {
        create(PUT, url, entity, token);
    }

    public void delete(String url, String token) {
        create(DELETE, url, null, token);
    }

    public void bitmap(String url) {
        create(BITMAP, url, null, token);
    }

    @Override
    public void run() {
        handler.sendMessage(Message.obtain(handler, HttpConnectionUtils.DID_START));
        HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 6000);
        try {
            HttpResponse response = null;
            String basuuid = "";
            switch (method) {
                case GET:
                    HttpGet httpGet = new HttpGet(url);
                    if (token != null) {
                        httpGet.addHeader("X-XSRF-TOKEN", token);
                    }
                    httpGet.addHeader("X-DEVICE-UNIONID", basuuid);
                    httpGet.addHeader("Accept", "application/json");
                    httpGet.addHeader("Content-Type", "application/json; charset=utf-8");
                    response = httpClient.execute(httpGet);
                    break;
                case POST:

                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(entity);
                    if (token != null) {
                        httpPost.addHeader("X-XSRF-TOKEN", token);
                    }
                    httpPost.addHeader("X-DEVICE-UNIONID", basuuid);
                    httpPost.addHeader("Accept", "application/json");
                    httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
                    response = httpClient.execute(httpPost);
                    break;
                case PUT:
                    HttpPut httpPut = new HttpPut(url);
                    httpPut.setEntity(entity);
                    if (token != null) {
                        httpPut.addHeader("X-XSRF-TOKEN", token);
                    }
                    httpPut.addHeader("X-DEVICE-UNIONID", basuuid);
                    httpPut.addHeader("Accept", "application/json");
                    httpPut.addHeader("Content-Type", "application/json; charset=utf-8");
                    response = httpClient.execute(httpPut);
                    break;
                case DELETE:
                    HttpDelete del = new HttpDelete(url);
                    if (token != null) {
                        del.addHeader("X-XSRF-TOKEN", token);
                    }
                    del.addHeader("X-DEVICE-UNIONID", basuuid);
                    del.addHeader("Accept", "application/json");
                    del.addHeader("Content-Type", "application/json; charset=utf-8");
                    response = httpClient.execute(del);
                    break;
                case BITMAP:
                    response = httpClient.execute(new HttpGet(url));
                    processBitmapEntity(response.getEntity());
                    break;
            }
            if (method != BITMAP) {
                HttpEntity entity = response != null ? response.getEntity() : null;
                processEntity(entity);
            }
        } catch (Exception e) {
            handler.sendMessage(Message.obtain(handler,
                    HttpConnectionUtils.DID_ERROR, e));
        }
        ConnectionManager.getInstance().didComplete(this);
    }

    private void processEntity(HttpEntity entity) throws IllegalStateException,
            IOException {
        String line, result = "";
        if (entity != null) {
            InputStream inputStream = entity.getContent();
            if (entity.getContentEncoding() != null && entity.getContentEncoding().getValue().contains("gzip")) {
                inputStream = new GZIPInputStream(inputStream);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null)
                result += line;
        }
        Message msg = new Message();
        msg.setTarget(handler);
        msg.what = DID_SUCCEED;
        msg.obj = result;
        Bundle b = new Bundle();
        b.putInt("request", number);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    private void processBitmapEntity(HttpEntity entity) throws IOException {
        BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
        Bitmap bm = BitmapFactory.decodeStream(bufHttpEntity.getContent());
        handler.sendMessage(Message.obtain(handler, DID_SUCCEED, bm));
    }

}
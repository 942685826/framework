package com.yaxon.frameWork.utils;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * 文件上传
 *
 * @author guojiaping
 * @version 2016/9/12.
 */
public class UploadFileUtil {
    private static final String TAG = "UploadUtil";
    private static final int TIME_OUT = 10 * 1000;
    private static final String CHARSET = "utf-8";
    private static final String CONTENT_TYPE = "multipart/form-data";
    private static final String REQUEST_METHOD = "post";
    private static final String BOUNDARY = UUID.randomUUID().toString();
    ;

    public static HttpURLConnection getHttpUrlConnection(String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setRequestProperty("Charset", CHARSET);
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            return connection;
        } catch (Exception ex) {

        }
        return null;
    }

    public static String uploadFile(File file, String requestURL) {
        String result = null;
        String PREFIX = "--";
        String LINE_END = "\r\n";
        try {
            HttpURLConnection connection = getHttpUrlConnection(requestURL);
            if (file != null) {
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition;form-data;name=\"img\";filename\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type;application/octet-stream;charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();

                int res = connection.getResponseCode();
                Log.e(TAG, "response code:" + res);
                if (res == 200) {
                    Log.e(TAG, "response success");
                    InputStream input = connection.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    Log.e(TAG, "result:" + result);
                }
            }
        } catch (Exception ex) {

        }
        return result;

    }

}


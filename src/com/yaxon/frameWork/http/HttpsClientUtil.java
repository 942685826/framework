package com.yaxon.frameWork.http;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.security.KeyStore;

/**
 * https连接
 *
 * @author guojiaping 2016-8-20 创建<br>
 */
public class HttpsClientUtil {
    private static final int SET_CONNECTION_TIMEOUT = 5 * 1000;
    private static final int SET_SOCKET_TIMEOUT = 20 * 1000;
    private static final String tag = "HttpsClientUtil";

    public static HttpClient getNewHttpClient() {
        /*************************************************/
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("getprop ro.kernel.qemu");
            os = new DataOutputStream(process.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*************************************************/
        try {

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            HttpConnectionParams.setConnectionTimeout(params, SET_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);
            HttpClient client = new DefaultHttpClient(ccm, params);

            return client;
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    /***
     * @param url
     * @param msg
     * @param timeout 超时时间
     * @return 响应消息
     * @throws IOException
     */
    public static String httpPost(String url, String msg, int timeout) throws Exception {
        // 参数
        HttpParams httpParameters = new BasicHttpParams();
        // 设置连接超时
        HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
        // 设置socket超时
        HttpConnectionParams.setSoTimeout(httpParameters, 30000);

        HttpClient httpClient = getNewHttpClient();
        /*httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(timeout));
        httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(timeout));
        httpClient.getParams().setParameter("http.protocol.handle-redirects", Boolean.valueOf(false));*/
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-type", "application/json;charset=utf-8");
        httpPost.addHeader("Accept", "application/json");
        StringEntity entity = new StringEntity(msg, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setParams(httpParameters);

        Log.v(tag, "msg:" + msg);
        HttpResponse httpResponse = null;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                StringBuffer buffer = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
//                if(line != null) {
                result = buffer.toString();
//                }
            }

        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
//        StringBuffer buffer = new StringBuffer();
//        String line = null;
//        while ((line = reader.readLine()) != null){
//            buffer.append(line);
//        }
        Log.v(tag, "result:" + result);
        if (result == "") {
            return null;
        } else {
            return result;
        }
    }

    /***
     * @param url
     * @param msg
     * @param timeout 超时时间
     * @return 响应消息
     * @throws IOException
     */
    public static String httpGet(String url, String msg, int timeout) throws Exception {
        // 参数
        HttpParams httpParameters = new BasicHttpParams();
        // 设置连接超时
        HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
        // 设置socket超时
        HttpConnectionParams.setSoTimeout(httpParameters, 30000);

        HttpClient httpClient = getNewHttpClient();
        /*httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(timeout));
        httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(timeout));
        httpClient.getParams().setParameter("http.protocol.handle-redirects", Boolean.valueOf(false));*/

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-type", "application/json;charset=utf-8");
        httpGet.addHeader("Accept", "application/json");
        StringEntity entity = new StringEntity(msg, "UTF-8");
        httpGet.setParams(httpParameters);

        HttpResponse httpResponse = null;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                StringBuffer buffer = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                result = buffer.toString();
            }

        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v(tag, "result:" + result);
        if (result == "") {
            return null;
        } else {
            return result;
        }
    }


}

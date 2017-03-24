package com.yaxon.frameWork.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author guojiaping
 * @version 2015/5/28 创建<br>
 */

public class HttpHandler extends Handler {

    private Context context;
    private ProgressDialog progressDialog;

    public HttpHandler(Context context) {
        this.context = context;
    }

    protected void start() {
        System.out.println("getActivity:" + context);
        progressDialog = ProgressDialog.show(context,
                "Please Wait...", "processing...", true);
    }


    protected void succeed(String result, int request) {

    }

    protected void failed(JSONObject jObject) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void otherHandleMessage(Message message) {
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case HttpConnectionUtils.DID_START: //connection start
                break;
            case HttpConnectionUtils.DID_SUCCEED: //connection success
                String response = (String) message.obj;
                succeed(response, message.getData().getInt("request"));
                break;
            case HttpConnectionUtils.DID_ERROR: //connection error
                Exception e = (Exception) message.obj;
                e.printStackTrace();
                break;
        }
        otherHandleMessage(message);
    }

}
package com.yaxon.frameWork.utils.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;
import com.yaxon.frameWork.debug.LogUtils;

import java.util.ArrayList;

/**
 * 发送短信
 *
 * @author liupei 2017-3-22 创建 <br>
 */
public class SMSSendUtils {
    private static SMSSendUtils mSmsSend = null;
    private static BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context _context, Intent _intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    LogUtils.d("HttpRequest", "SMS sent success actions");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    LogUtils.e("HttpRequest", "SMS generic failure actions");
                    break;
                default:
                    break;
            }
        }
    };

    private static BroadcastReceiver mBroadcastDeliverReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "SMS delivered actions", Toast.LENGTH_SHORT).show();
        }
    };;

    /**
     * 获取实例
     *
     * @return
     */
    public static SMSSendUtils getInstance() {
        if (mSmsSend == null) {
            mSmsSend = new SMSSendUtils();
        }
        return mSmsSend;
    }

    /**
     * 发送短信
     *
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(final Context context, String phoneNumber, String message) {

        SmsManager sms = SmsManager.getDefault();

        Intent sentIntent = new Intent("SMS_SEND_ACTION");
        Intent deliverIntent = new Intent("SMS_DELIVERED_ACTION");

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);



        if (message.length() > 70) {// 短信度大于70字
            ArrayList<String> msgs = sms.divideMessage(message); // 拆分信息
            for (String msg : msgs) {
                sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
            }
        } else {
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
        }
    }

    /**
     * 注册监听
     * @param context
     */
    public void register(Context context) {
        context.registerReceiver(mBroadcastReceiver, new IntentFilter("SMS_SEND_ACTION"));
        context.registerReceiver(mBroadcastDeliverReceiver, new IntentFilter("SMS_DELIVERED_ACTION"));
    }

    /**
     * 界面关闭时取消注册监听
     *
     * @param context
     */
    public void unRegister(Context context) {
        context.unregisterReceiver(mBroadcastDeliverReceiver);
        context.unregisterReceiver(mBroadcastReceiver);
    }
}

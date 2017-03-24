package com.yaxon.frameWork.utils.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.yaxon.frameWork.debug.LogUtils;

/**
 * 短信接收广播
 *
 * @author liupei 2015-7-14 创建 <br>
 */
public class SmsReceive extends BroadcastReceiver {
    private final static String TAG = "SmsReceive";
    private static SmsReceive mSmsRece;
    private String mobile;

    /**
     * 获取实例
     *
     * @return
     */
    public static SmsReceive getInstance() {
        if (mSmsRece == null) {
            mSmsRece = new SmsReceive();
        }
        return mSmsRece;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];
            for (int n = 0; n < messages.length; n++) {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]); //还原短信内容
                String msgBody = smsMessage[n].getMessageBody();//取得短信内容
                mobile = smsMessage[n].getOriginatingAddress();
                LogUtils.i(TAG, "msgBody=" + msgBody);
                if(smsListener != null) {
                    smsListener.receive(context, msgBody);
                }
                this.abortBroadcast();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 界面关闭时取消注册监听
     *
     * @param context
     */
    public void unRegister(Context context) {
        context.unregisterReceiver(mSmsRece);
    }

    //注册短信接收
    public void register(Context context) {
        context.registerReceiver(SmsReceive.getInstance(), new IntentFilter("android.intent.action.DATA_SMS_RECEIVED"));
        context.registerReceiver(SmsReceive.getInstance(), new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    private SmsListener smsListener;
    public interface SmsListener {
        void receive(Context context, String message);
    }
    public void setSmsListener(SmsListener smsListener){
        this.smsListener = smsListener;
    }
}

package com.yaxon.frameWork.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import com.yaxon.frameWork.debug.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * 获取应用相关的信息
 *
 * @author guojiaping
 * @version 2015-4-20 创建<br>
 */
public class AppUtils {
    private AppUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序版本名称信息
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取WIFI MAC地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            return null;
        }
        String macAddress = wifiInfo.getMacAddress();
        return macAddress;
    }

    /**
     * 获取IMsi号
     *
     * @param context
     * @return
     */
    public static String getPhoneIMSI(Context context) {
        String IMsi = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getSubscriberId();
        return IMsi;
    }

    /**
     * 获取IMEI号
     *
     * @param context
     * @return 设备的IMEI号
     */
    public static String getPhoneIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取AndroidId
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 获取设备UUID
     *
     * @param context
     * @return
     */
    public static String getUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();

        return uniqueId;
    }


    /**
     * 修改文件权限
     *
     * @param path 文件路径
     */
    public static void alterChmod(String path) {
        String permission = "777";
        String command = "chmod " + permission + " " + path;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 安装apk程序
     *
     * @param path    文件路径
     * @param context 程序上下文
     */
    public static void installApk(String path, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载apk程序
     *
     * @param packageName 包名
     * @param context     程序上下文
     */
    public static void uninstallApk(String packageName, Context context) {
        Uri packageUri = Uri.parse("package:" + packageName);
//        Uri packageUri = Uri.parse("package: com.yaxon.frameWork");
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
        context.startActivity(uninstallIntent);
    }

    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber 手机号码
     * @param message     短信消息
     * @param context     上下文
     */
    public static void sendSMS(String phoneNumber, String message, Context context) {
        String imsi = getPhoneIMSI(context);
        if (imsi != null) {//判断是否存在sim卡
            //获取短信管理器
            SmsManager smsManager = SmsManager.getDefault();

            //拆分短信内同（手机短信长度限制
            List<String> divideContents = smsManager.divideMessage(message);
            for (String text : divideContents) {
                smsManager.sendTextMessage(phoneNumber, null, text, null, null);
            }
        } else {
            ToastUtils.showShort(context, "未检测到sim卡，请检查");
        }
    }

    /**
     * 通过代码实现像adb shell中执行命令
     *
     * @param command
     * @return
     */
    public static String execCommand(String... command) {
        Process process;
        InputStream errIs;
        InputStream inIs;
        String result = "";

        try {
            process = new ProcessBuilder().command(command).start();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            result = new String(baos.toByteArray());
            if (inIs != null) {
                inIs.close();
            }
            if (errIs != null) {
                errIs.close();
            }
        } catch (IOException e) {
            result = e.getMessage();
        }
        return result;
    }

    public static String execInstallCommand(String path) {
        String result = execCommand("pm", "install", "-f", path);
        return result;
    }

    public static String execUnInstallCommand(String packageName) {
        String result = execCommand("pm", "uninstall", packageName);
        return result;
    }

    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber 手机号码
     * @param message     短信消息
     * @param context     上下文
     */
    public static void doSendSMSto(String phoneNumber, String message, Context context) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Uri uri = Uri.parse("smsto:" + phoneNumber);
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", message);
            context.startActivity(intent);
        }
    }

    /**
     * 处理发送消息返回状态
     *
     * @param context
     */
    public static void SMSReceive(Context context) {
        String SEND_SMS_ACTION = "SEND_SMS_ACTION";
        Intent sendIntent = new Intent(SEND_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sendIntent, 0);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        ToastUtils.showShort(context, "发送成功");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                }
            }
        }, new IntentFilter(SEND_SMS_ACTION));
    }

    /**
     * 拨打电话
     *
     * @param phoneNumber 电话号码
     * @param context
     */
    public static void callPhone(String phoneNumber, Context context) {
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent("android.intent.action.CALL", uri);
        context.startActivity(intent);
    }
}

package com.yaxon.frameWork.debug;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 捕捉工程中未捕捉到的异常
 *
 * @author guojiaping
 * @version 2016-5-11 创建<br>
 */

public class CrashExceptionHandler implements UncaughtExceptionHandler {
    private static final String TAG = CrashExceptionHandler.class.getSimpleName();
    private UncaughtExceptionHandler mDefaultHandler; //系统默认的UncaughtException处理�?
    private static CrashExceptionHandler mInstance;//CrashHandler实例
    private Context mContext; //程序的Context对象
    private String path = "/sdcard/safetyWarning/crash/";
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();

    //用于格式日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private Callback callback;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashExceptionHandler() {

    }

    public static CrashExceptionHandler getInstance() {
        if (mInstance == null) {
            mInstance = new CrashExceptionHandler();
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context, String path) {
        mContext = context;
        this.path = path;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        LogUtils.d(TAG, "crashException init");
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // TODO Auto-generated method stub
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);

        } else {
            if (this.callback != null)
                this.callback.execute();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 设置异常回调接口
     *
     * @param callback 实现回调函数
     */
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {

        if (ex == null) {
            return true;
        }
        LogUtils.e(TAG, ex.getMessage() + ex.getCause());
        collectDeviceInfo(mContext);
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    private void collectDeviceInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String versionCode = packageInfo.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                LogUtils.d(TAG, versionName + ":" + versionCode);
            }
        } catch (PackageManager.NameNotFoundException ex) {
            LogUtils.e(TAG, ex.toString());
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                LogUtils.d(TAG, field.getName() + ":" + field.get(null));
            } catch (Exception e) {
                LogUtils.e(TAG, "an error occuered when collect crash info" + e.toString());
            }
        }
    }


    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称，全球将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            LogUtils.d(TAG, fileName);
            return fileName;
        } catch (Exception e) {
            LogUtils.e(TAG, "an error occured while writing file..." + e);
        }
        return null;
    }

    /**
     * 异常回调类
     */
    public interface Callback {
        void execute();
    }
}

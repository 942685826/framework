package com.yaxon.frameWork.service;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import com.yaxon.frameWork.debug.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 将服务设置为前台进程(状态栏发送通知)
 *
 * @author guojiaping 2016-10-18 创建<br>
 */
public class Foreground {

    private NotificationManager notificationManager;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];
    private static final Class[] mStartForegroundSignature = new Class[]{
            int.class, Notification.class};
    private static final Class[] mStopForegroundSignature = new Class[]{
            boolean.class};
    private Context context;
    private static final int NOTIFICATION_ID = 1; // 如果id设置为0,会导致不能设置为前台service

    /**
     * 服务设置为前台进程
     *
     * @param context    上下午
     * @param cls        服务对应的类
     * @param activity
     * @param drawableId 通知图标
     */
    public void initNotification(Context context, Class cls, Class activity, int drawableId) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            mStartForeground = cls.getMethod("startForeground",
                    mStartForegroundSignature);
            mStopForeground = cls.getMethod("stopForeground",
                    mStopForegroundSignature);
            this.context = context;
        } catch (Exception ex) {
            mStartForeground = mStopForeground = null;
        }
        Notification.Builder builder = new Notification.Builder(context);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//                    new Intent(context, MainActivity.class), 0);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, activity), 0);
        builder.setContentIntent(contentIntent);

        builder.setSmallIcon(drawableId);
//        builder.setSmallIcon(R.drawable.icon);
        builder.setTicker("Foreground Service Start");
        builder.setContentTitle("Foreground Service");
        builder.setContentText("Make this service run in the foreground.");
        Notification notification = builder.build();

        startForegroundCompat(NOTIFICATION_ID, notification);
    }

    private void startForegroundCompat(int id, Notification notification) {
        if (mStartForeground != null) {
            mStartForegroundArgs[0] = id;
            mStartForegroundArgs[1] = notification;
            try {
                mStartForeground.invoke(context, mStartForegroundArgs);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        notificationManager.notify(id, notification);
    }

    public void stopForegroundCompat() {
        if (mStartForeground != null) {
            mStartForegroundArgs[0] = Boolean.TRUE;
            try {
                mStopForeground.invoke(context, mStopForegroundArgs);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }

        notificationManager.cancel(NOTIFICATION_ID);
    }

    /**
     * 判断是前台服务还是后台服务
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    LogUtils.i("后台", appProcess.processName);
                    return true;
                } else {
                    LogUtils.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

}

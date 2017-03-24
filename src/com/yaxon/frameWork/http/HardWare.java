/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.yaxon.frameWork.http;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.*;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.lang.Process;

/**
 * 硬件设备类
 *
 * @author zzh 2012.11.15 创建<br>
 */
public class HardWare {
    public static final String PHONE_MODEL_T21 = "W9778";
    public static final String PHONE_MODEL_T55 = "C9781";

    public static final String PHONE_MODEL_C26 = "Y9883SPDJB";
    public static final String PHONE_MODEL_C29 = "C29";
    public static final String PHONE_MODEL_CG29 = "CG29";
    public static final String PHONE_MODEL_EQ863 = "EQ863";

    // private static final String tag = HardWare.class.getSimpleName();
    private static int mScreenHeight = 0; // 竖屏时屏幕的高度
    // （对于大屏的pad，屏幕的高度会减去底部状态栏的高度，因此切换横竖屏时宽高数据要重新获取）
    private static int mScreenWidth = 0; // 竖屏时屏幕的宽度
    private static int mScreenDensity = 0;
    private static int mScreenHorizHeight = 0; // 当时横屏时，重新获取屏幕大小（针对大屏的pad，屏幕的高度会除去底部状态栏的高度）
    private static int mScreenHorizWidth = 0;
    private static String mImei = null;
    private static DeviceInfo deviceInfo = null;

    /**
     * 构造函数
     */
    private HardWare() {

    }

    /**
     * 测试是否为yaxon手机
     *
     * @return true：yaxon手机，false：通用手机
     */
    public static boolean isYaxonMobie() {
        if (Build.MODEL.contains("C9781") || Build.MODEL.contains("W9778")
                || Build.MODEL.contains("Y9883SPDJB") || Build.MODEL.contains("EQ863")
                || Build.MODEL.contains(PHONE_MODEL_C29)) {
            return true;
        }
        return false;
    }

    /**
     * 获取WIFI MAC地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        WifiManager wifimanager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifimanager == null) {
            return null;
        }

        WifiInfo wifiinfo = wifimanager.getConnectionInfo();
        if (wifiinfo == null) {
            return null;
        }
        String addr = wifiinfo.getMacAddress();
        return addr;
    }

    /**
     * 获取IMSI号
     *
     * @param context
     * @return
     */
    public static String getPhoneIMSI(Context context) {
        String mIMSI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getSubscriberId();
        return mIMSI;
    }


    /**
     * 获取IMEI号
     *
     * @return 设备的IMEI号
     */
    public static String getPhoneIMEI() {
        return mImei == null ? "" : mImei;
    }

    /**
     * 设置IMEI号
     *
     * @param imei 设备的IMEI号
     */
    public static void setPhoneIMEI(String imei) {
        if (imei != null) {
            mImei = new String(imei);
        }
    }

    /**
     * 获取屏幕相关参数
     */
    public static void initScreen(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(dm);
        if (dm.widthPixels > dm.heightPixels) {
            int pixel = dm.widthPixels;
            dm.widthPixels = dm.heightPixels;
            dm.heightPixels = pixel;
        }

        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mScreenDensity = dm.densityDpi;

    }



    /**
     * 获取屏幕密度
     *
     * @return
     */
    public static int getScreenDensity() {
        return mScreenDensity;
    }

    /**
     * SD卡是否有效
     *
     * @return
     */

    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取内置SD可用的存储空间
     *
     * @return 大小(MB)
     */
    public static float getSDCardSize() {
        String state = Environment.getExternalStorageState();
        // SD卡不可用
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return 0;
        }

        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        float size = (availableBlocks * blockSize) * 1.0f / (1024 * 1024);
        return size;
    }

    /**
     * 内置SD卡剩余空间是否够
     *
     * @param size
     * @return
     */
    public static boolean isSDCardSizeEnough(float size) {
        if (getSDCardSize() > size) {
            return true;
        }
        return false;
    }

    /**
     * 获取外置SD卡可用的存储空间
     *
     * @return 大小(MB)
     */
    public static float getExtSDCardSize() {
        String path = getExtSDCardPath();
        if (path == null) {
            return 0;
        }
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        float size = (availableBlocks * blockSize) * 1.0f / (1024 * 1024);
        return size;
    }

    /**
     * 外置SD卡剩余空间是否够
     *
     * @param size
     * @return
     */
    public static boolean isExtSDCardSizeEnough(int size) {
        if (getExtSDCardSize() > size) {
            return true;
        }
        return false;
    }

    /**
     * 获取可用的手机内部存储空间(ROM)
     *
     * @return 大小(MB)
     */
    public static float getAvailableInternalStorage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        float size = (availableBlocks * blockSize) * 1.0f / (1024 * 1024);
        return size;
    }

    /**
     * 获取手机内部存储空间大小(ROM)
     *
     * @return 大小(MB)
     */
    public static float getTotalInternalStorage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        float size = (totalBlocks * blockSize) * 1.0f / (1024 * 1024);
        return size;
    }

    /**
     * 获取可用的手机运行存储空间(RAM)
     *
     * @return 大小(MB)
     */
    public static float getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem * 1.0f / (1024 * 1024);
    }

    /**
     * 获取手机运行存储空间大小(RAM)
     *
     * @return 大小(MB)
     */
    public static float getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            // 读取meminfo第一行，系统总内存大小
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");

            // 获得系统总内存, 单位是KB
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue();
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return initial_memory * 1.0f / 1024;
    }

    /**
     * 手机内部存储空间是否足够
     *
     * @param size
     * @return
     */
    public static boolean isInternalStorageEnough(int size) {
        if (getAvailableInternalStorage() > size) {
            return true;
        }
        return false;
    }

    /**
     * 存储空间是否足够,包括SD和手机内部Rom存储
     *
     * @param size 要求大小 (MB)
     * @return
     */
    public static boolean isStorageEnough(int size) {
        if (getSDCardSize() > size || getExtSDCardSize() > size
                || getAvailableInternalStorage() > size) {
            return true;
        }
        return false;
    }




    /**
     * 清除系统内存
     *
     * @param context
     */
    public static void clearMem(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return;
        }

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // YXLog.e(tag, "pid " + appProcess.pid + appProcess.processName);
            // YXLog.e(tag, "importance " + appProcess.importance);

            if ("system".equals(appProcess.processName)
                    || "android.process.media".equals(appProcess.processName)
                    || "android.process.acore".equals(appProcess.processName)
                    || "com.android.phone".equals(appProcess.processName)
                    || appProcess.processName.startsWith("com.android.inputmethod")
                    || appProcess.processName.startsWith("com.yaxon.crm")
                    || appProcess.processName == null) {
                continue;
            }
            if (appProcess.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                // android.os.Process.killProcess(appProcess.pid);
                // YXLog.e(tag, "Kill " + appProcess.processName);
                activityManager.killBackgroundProcesses(appProcess.processName);
            }
        }
    }

    /**
     * SIM卡是否有效
     *
     * @param context
     * @return
     */
    public static boolean isSIMCardAvailable(Context context) {
        if (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getSimState() != 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取外置SD路径
     *
     * @return
     */
    public static String getExtSDCardPath() {
        ArrayList<String> sdCardPathArrayList = getSDCardsPath();
        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
        if (sdCardPathArrayList == null || sdCardPathArrayList.size() < 1) {
            return null;
        }
        for (String extSDCardPath : sdCardPathArrayList) {
            if (!extSDCardPath.equals(sdCardPath)) {
                return extSDCardPath;
            }
        }

        return null;
    }

    /**
     * 获取内置SD路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取全部SD卡路径
     *
     * @return
     */
    private static ArrayList<String> getSDCardsPath() {
        ArrayList<String> mount = new ArrayList<String>();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;

            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure")) {
                    continue;
                }
                if (line.contains("asec")) {
                    continue;
                }
                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        mount.add(columns[1]);
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        mount.add(columns[1]);
                    }
                }
            }
            is.close();
            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mount;
    }


    /**
     * 关机
     *
     * @param context
     */
    public static void powerOff(Context context) {

    }

    public static void sendMessage(Handler handler, int i) {
        if (handler == null) {
            return;
        }
        Message.obtain(handler, i).sendToTarget();
    }

    public static void sendMessage(Handler handler, int i, int j, int k) {
        if (handler == null) {
            return;
        }
        Message.obtain(handler, i, j, k).sendToTarget();
    }

    public static void sendMessage(Handler handler, int i, int j, int k,
                                   Object obj) {
        if (handler == null) {
            return;
        }
        Message.obtain(handler, i, j, k, obj).sendToTarget();
    }

    public static void sendMessage(Handler handler, int i, Object obj) {
        if (handler == null) {
            return;
        }
        Message.obtain(handler, i, obj).sendToTarget();
    }

    public static void sendMessageDelayed(Handler handler, int i, int j) {
        if (handler == null) {
            return;
        }
        handler.sendMessageDelayed(Message.obtain(handler, i), j);
    }

    public class DeviceInfo {
        /**
         * 获取主板信息
         *
         * @return
         */
        public String getBoard() {
            return Build.BOARD;
        }

        /**
         * 获取 android系统定制商
         *
         * @return
         */
        public String getBrand() {
            return Build.BRAND;
        }

        /**
         * 获取 cpu指令集
         *
         * @return
         */
        public String getCpuAbi() {
            return Build.CPU_ABI;
        }

        /**
         * 获取设备参数
         *
         * @return
         */
        public String getDevice() {
            return Build.DEVICE;
        }

        /**
         * 获取 硬件制造商
         *
         * @return
         */
        public String getManufacture() {
            return Build.MANUFACTURER;
        }

        /**
         * 获取 版本
         *
         * @return
         */
        public String getModel() {
            return Build.MODEL;
        }

        /**
         * 获取 硬件名称，包括平台版本号
         *
         * @return
         */
        public String getFingerPrint() {
            return Build.FINGERPRINT;
        }

        /**
         * 获取 手机制造商即手机型号
         *
         * @return
         */
        public String getProduct() {
            return Build.PRODUCT;
        }

        /**
         * 获取修订版本列表
         *
         * @return
         */
        public String getID() {
            return Build.ID;
        }

        /**
         * 获取builder类型
         *
         * @return
         */
        public String getType() {
            return Build.TYPE;
        }
    }

    public DeviceInfo getDeviceInfo() {
        if (deviceInfo == null) {
            deviceInfo = new DeviceInfo();
        }
        return deviceInfo;
    }

    /**
     * C26/PAD拍照声音控制
     *
     * @param context 上下文
     * @param enable  ：true打开声音,false关闭声音
     */
    private static void openCameraSound(Context context, boolean enable) {
        Intent intent = new Intent("camera_sound_turnoff");
        // "1" to disable camera sound, "0" to enable camera sound
        if (enable) {
            intent.putExtra("state", "0");
        } else {
            intent.putExtra("state", "1");
        }
        context.sendBroadcast(intent);
    }


    /**
     * 手机基本情况信息
     *
     * @return
     */
    public static String getDeviceInfoS(Context context) {
        StringBuffer sbDevice = new StringBuffer();
        // SD卡
        try {
            sbDevice.append("\nSD卡: ");
            sbDevice.append((int) HardWare.getSDCardSize());
            sbDevice.append("M / ");
            sbDevice.append((int) HardWare.getExtSDCardSize());
            sbDevice.append("M");
        } catch (Exception e) {
            //YXLog.d("未获得读取SD卡信息权限");
        }

        // ROM
        try {
            sbDevice.append("\n内部存储: ");
            sbDevice.append((int) HardWare.getAvailableInternalStorage());
            sbDevice.append("M / ");
            sbDevice.append((int) HardWare.getTotalInternalStorage());
            sbDevice.append("M");
        } catch (Exception e) {
//            YXFile.writeLog("未获得读取内部存储信息权限");
        }

        // RAM
        try {
            sbDevice.append("\n运行内存: ");
            sbDevice.append((int) HardWare.getAvailMemory(context));
            sbDevice.append("M / ");
            sbDevice.append((int) HardWare.getTotalMemory(context));
            sbDevice.append("M");
        } catch (Exception e) {
//            YXFile.writeLog("未获得读取运行内存信息权限");
        }
//        YXFile.writeLog(sbDevice.toString());
        return sbDevice.toString();
    }

}

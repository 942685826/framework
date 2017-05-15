package com.yaxon.frameWork.file;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取SD卡相关信息
 *
 * @author guojiiaping
 * @version 2016/11/11 创建<br>
 */
public class SDCardUtils {
    private SDCardUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    /**
     * 获取内置SD卡路�?
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath()
                + File.separator;
    }

    /**
     * 获取外置SD卡路�?
     *
     * @return
     */
    public static String getExtSDCardPath() {
        ArrayList<String> sdCardPathArrayList = getSDCardsPath();
        String sdCardPath = getSDCardPath();
        if (sdCardPathArrayList != null && sdCardPathArrayList.size() > 0) {
            for (String extSDCardPath : sdCardPathArrayList) {
                return extSDCardPath;
            }
        }
        return null;
    }

    /**
     * 获取全部SD卡路�?
     *
     * @return
     */
    private static ArrayList<String> getSDCardsPath() {
        ArrayList<String> mount = new ArrayList<>();
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            is = proc.getInputStream();
            isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure") || line.contains("asec")) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (Exception e) {
            }
        }
        return mount;
    }


    /**
     * 获取内置SD卡的剩余容量（MB�?
     *
     * @return
     */
    public static float getSDCardAvailableSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            //获取空闲的数据块的数�?
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            //获取单个数据块的大小（byte�?
            long freeBlocks = stat.getAvailableBlocks();
            return (freeBlocks * availableBlocks) * 1.0f / (1024 * 1024);
        }
        return 0;
    }

    /**
     * 获取外置SD卡可用的存储空间
     *
     * @return
     */
    public static float getExtSDCardSize() {
        String path = getExtSDCardPath();
        if (path == null) {
            return 0;
        }
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        long availableBlock = statFs.getAvailableBlocks();
        return (blockSize * availableBlock) * 1.0f / (1024 * 1024);
    }

    /**
     * 获取可用的手机运行存储空间（RAM�?
     *
     * @return 大小(MB)
     */
    public static float getAvailableInternalStorage() {
        String path = Environment.getDataDirectory().getPath();
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        long totalBlocks = statFs.getBlockCount();
        return (blockSize * totalBlocks) * 1.0f / (1024 * 1024);
    }

    /**
     * 获取手机运行存储空间（RAM�?
     *
     * @return 大小(MB)
     */
    public static float getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";//系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initialMemory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader bufferedReader = new BufferedReader(localFileReader);
            //读取meminfo第一行，系统总内存大�?
            str2 = bufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            //获得系统总内存，单位是KB
            initialMemory = Integer.valueOf(arrayOfString[1]).intValue();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return initialMemory * 1.0f / 1024;
    }


    /**
     * 获取当前系统内存大小（MB�?
     *
     * @param context
     * @return
     */
    public static long getAvailableMem(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long num = memoryInfo.availMem / (1024 * 1024);
        return num;
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
     * @param size 要求大小
     *             (MB)
     * @return
     */
    public static boolean isStorageEnough(int size) {
        if (getSDCardAvailableSize() > size || getExtSDCardSize() > size
                || getAvailableInternalStorage() > size) {
            return true;
        }
        return false;
    }


    /**
     * 清除系统缓存
     *
     * @param context
     */
    public static void clearMem(Context context) {
        if (getAvailableMem(context) > 20) {
            return;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if ("system".equals(appProcess.processName)
                    || "android.process.media".equals(appProcess.processName)
                    || "android.process.acor".equals(appProcess.processName)
                    || appProcess.processName == null
                    || appProcess.processName.startsWith("com.android.inputmethod")
                    || appProcess.processName.startsWith("com.yaxon.crm")) {
                continue;
            }
            if (appProcess.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                activityManager.killBackgroundProcesses(appProcess.processName);
            }
            if (getAvailableMem(context) > 30) {
                break;
            }
        }
    }

    public static boolean isSIMCardAvailable(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getSimState() != 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取指定路径�?在空间的剩余可用容量字节�?
     *
     * @param filePath
     * @return 容量字节，SD卡可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        //如果是sd卡下的路径，则获取sd卡可用容�?
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {//如果是内部存储的路径，则获取内部存储的可用容�?
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        long freeBlocks = stat.getAvailableBlocks();
        return freeBlocks * availableBlocks;
    }

}

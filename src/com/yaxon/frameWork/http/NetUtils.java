package com.yaxon.frameWork.http;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.yaxon.frameWork.debug.LogUtils;

import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 网络状态判断
 *
 * @author guojiaping
 * @version 2016-11-9 创建<br>
 */
public class NetUtils {
    private static final String tag = NetUtils.class.getSimpleName();
    private static final Uri LIST_APN_URI = Uri.parse("content://telephony/carriers");
    private static final Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private static String mApnName = NetAPN.UNKNOWN;
    private static String mProxy = "";
    private static String mPort = "";
    private static String mCurUser = "";

    private NetUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否有网络，需要加上访问网络状态的权限
     *
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是WiFi网络
     *
     * @param context
     * @return
     */
    public static boolean isWifeNet(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 打开网络设置界面
     *
     * @param activity
     */
    public static void openSetting(Activity activity) {
        //整体
        activity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        //WIFI
        //activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        //流量
        //activity.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));

    }

    /**
     * 使用WiFi获取ip地址
     *
     * @param context
     * @return
     */
    public static String getWifiAddress(Context context) {
        //获取WiFi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断WiFi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    private static String intToIp(int i) {
        String ipAddress = (i & 0xFF) + "." + ((i >> 8) & 0xFF)
                + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
        return ipAddress;
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getHostIp() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress inetAddress;
            while (nis.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = networkInterface.getInetAddresses();
                while (ias.hasMoreElements()) {
                    inetAddress = ias.nextElement();
                    if (inetAddress instanceof Inet6Address) {
                        continue;//skip ipv6
                    }
                    String ip = inetAddress.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = inetAddress.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIp;
    }

    /**
     * 禁止常连接
     *
     * @param context
     */
    public static void disableConnectionReuseIfNecessary(Context context) {
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * 获取当前APN名字
     *
     * @param context
     * @return APN
     */
    public static String getCurrentAPNFromSetting(Context context) {
        String curApnId = null;
        Cursor cursor = null;

        if (isWifiAvailable(context)) {
            mApnName = NetAPN.UNKNOWN;
            return mApnName;
        }
        // 4.0SDK之后只有系统应用才有读写APN权限
        if (Build.VERSION.SDK_INT >= 14) {
            mApnName = NetAPN.UNKNOWN;
            return mApnName;
        }

        try {
            cursor = context.getContentResolver().query(CURRENT_APN_URI, null,
                    null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        curApnId = cursor.getString(cursor.getColumnIndex("_id"));
                        mApnName = cursor.getString(cursor.getColumnIndex("apn"));
                        mProxy = cursor.getString(cursor.getColumnIndex("proxy"));
                        mPort = cursor.getString(cursor.getColumnIndex("port"));
                        if (mApnName != null && mApnName.equals("#777")) {
                            mCurUser = cursor.getString(cursor.getColumnIndex("user"));
                            if (mCurUser != null) {
                                if (mCurUser.startsWith(NetAPN.CTWAP)) {
                                    mApnName = NetAPN.CTWAP;
                                } else if (mCurUser.startsWith(NetAPN.CTNET)) {
                                    mApnName = NetAPN.CTNET;
                                }
                            }
                        }
                        if (curApnId != null && !mApnName.equals(NetAPN.UNKNOWN)) {
                            break;
                        }
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null) {
                cursor.close();
            }

            // find apn name from apn list
            if (mApnName == null || mApnName.equals(NetAPN.UNKNOWN)) {
                cursor = context.getContentResolver().query(LIST_APN_URI, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            mApnName = cursor.getString(cursor.getColumnIndex("apn"));
                            if (mApnName.equals("#777")) {
                                mCurUser = cursor.getString(cursor.getColumnIndex("user"));
                                if (mCurUser != null) {
                                    if (mCurUser.startsWith(NetAPN.CTWAP)) {
                                        mApnName = NetAPN.CTWAP;
                                    } else if (mCurUser.startsWith(NetAPN.CTNET)) {
                                        mApnName = NetAPN.CTNET;
                                    }
                                }
                            }
                            if (mApnName != null && !mApnName.equals(NetAPN.UNKNOWN)) {
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                }
            }

            LogUtils.v(tag, "curApnId:" + curApnId + " apnName:" + mApnName);
        } catch (SQLException e) {
            LogUtils.v(tag, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mApnName;
    }


    /**
     * 修改设置当前APN
     *
     * @param context
     * @param newAPN
     * @return
     */
    private static boolean updateCurrentAPN(Context context, String newAPN) {
        Cursor cursor = null;
        ContentResolver resolver;
        resolver = context.getContentResolver();

        // 4.0SDK之后只有系统应用才有读写APN权限
        if (Build.VERSION.SDK_INT >= 14) {
            return false;
        }

        // 只支持雅迅设备
        if (!HardWare.isYaxonMobie()) {
            return false;
        }

        try {
            // get new apn id from list
            if (newAPN.equals(NetAPN.CTNET)) {
                cursor = resolver.query(LIST_APN_URI, null, "user = ?",
                        new String[]{"ctnet@mycdma.cn"}, null);
            } else if (newAPN.equals(NetAPN.CTWAP)) {
                cursor = resolver.query(LIST_APN_URI, null, "user = ?",
                        new String[]{"ctwap@mycdma.cn"}, null);
            } else {
                cursor = resolver.query(LIST_APN_URI, null, " apn = ?",
                        new String[]{newAPN.toLowerCase()}, null);
            }

            String apnId = null;
            int num = cursor.getCount();
            if (cursor != null && num > 0) {
                cursor.moveToFirst();
                apnId = cursor.getString(cursor.getColumnIndex("_id"));
            }
            if (cursor != null) {
                cursor.close();
            }

            if (newAPN.equals(NetAPN.CMNET)) {
                apnId = "204";
            } else if (newAPN.equals(NetAPN.CMWAP)) {
                apnId = "212";
            } else {
                return false;
            }

            LogUtils.v(tag, "update apnId:" + apnId);
            // set new apn id as chosen one
            if (apnId != null) {
                ContentValues values = new ContentValues();
                values.put("apn_id", apnId);
                resolver.update(CURRENT_APN_URI, values, null, null);
                return true;
            }

        } catch (SQLException e) {
            LogUtils.v(tag, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    /**
     * 获取网络状态,如果当前APN是NET状态则自动切换为WAP APN是WAP状态则自动切换为NET
     *
     * @param context
     * @return
     */
    public static boolean switchAPNState(Context context) {
        boolean ret = false;

        String oldAPN = getCurrentAPNFromSetting(context);
        if (oldAPN == null) {
            oldAPN = NetAPN.UNKNOWN;
        }

        LogUtils.v(tag, "oldAPN:" + oldAPN);
        String newAPN = null;

        if (NetAPN.CTWAP.equals(oldAPN.toLowerCase())) {
            newAPN = NetAPN.CTNET;
        } else if (NetAPN.CMWAP.equals(oldAPN.toLowerCase())) {
            newAPN = NetAPN.CMNET;
        } else if (NetAPN.UNIWAP.equals(oldAPN.toLowerCase())) {
            newAPN = NetAPN.UNINET;
        } else if (NetAPN.G3WAP.equals(oldAPN.toLowerCase())) {
            newAPN = NetAPN.G3NET;

        } else if (NetAPN.CTNET.equals(oldAPN.toLowerCase())) {
            newAPN = NetAPN.CTWAP;
        } else if (NetAPN.CMNET.equals(oldAPN.toLowerCase())) {
            newAPN = NetAPN.CMWAP;
        } else if (NetAPN.UNINET.equals(oldAPN.toLowerCase())) {
            newAPN = NetAPN.UNIWAP;
        } else if (NetAPN.G3NET.equals(oldAPN.toLowerCase())) {
            newAPN = NetAPN.G3WAP;
        }

        if (newAPN != null) {
            ret = updateCurrentAPN(context, newAPN);
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获取APN名字
     *
     * @return
     */
    public static String getAPN() {
        return mApnName;
    }

    /**
     * 获取代理IP地址（WAP模式下才有代理网关IP地址,NET模式下没有）
     *
     * @return
     */
    public static String getProxyIp() {
        return mProxy;
    }

    /**
     * 获取代理端口地址（WAP模式下才有代理网关端口地址,NET模式下没有）
     *
     * @return
     */
    public static String getProxyPort() {
        return mPort;
    }

    /**
     * 获取本地IP地址
     *
     * @return
     * @throws SocketException
     */
    public static String getLocalIpAddress() throws SocketException {
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        if (!enumeration.hasMoreElements()) {
            return null;
        }

        Enumeration<InetAddress> enumeration1 = enumeration
                .nextElement().getInetAddresses();
        if (!enumeration1.hasMoreElements()) {
            return null;
        }

        InetAddress inetaddress = enumeration1.nextElement();
        if (!inetaddress.isLoopbackAddress()) {
            return null;
        }
        String ipaddress = inetaddress.getHostAddress().toString();
        return ipaddress;
    }

    /**
     * 判断WIFI是否开启
     */
    public static boolean isWiFiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            return true;
        }
        return false;
    }

    /**
     * WIFI是否有效
     *
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifimanager == null) {
            return false;
        }
        WifiInfo wifiinfo = wifimanager.getConnectionInfo();
        if (wifiinfo == null) {
            return false;
        }
        int ip = wifiinfo.getIpAddress();
        if (!wifimanager.isWifiEnabled() || ip == 0) {
            return false;
        }
        return true;
    }

    /**
     * WIFI是否已连接
     *
     * @param context
     * @return true已连接, false未连接
     */
    public static boolean isWIFIConnected(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        if (wi != null
                && (WifiInfo.getDetailedStateOf(wi.getSupplicantState()) == NetworkInfo.DetailedState.OBTAINING_IPADDR || WifiInfo
                .getDetailedStateOf(wi.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED)) {
            return true;
        }
        return false;
    }

    /**
     * 判断WIFI是否连接成功
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo == null) {
            return false;
        }
        return wifiNetworkInfo.isConnected();
    }

//    /**
//     * 反射调用网络数据开关接口(REACH提供)
//     *
//     * @param context ：上下文
//     * @param enable  ： true 开启; false 关闭
//     */
//    private static void setMobileDataEnable(Context context, boolean enable) {
//        try {
//            try {
//                Class<?> mConM = Class.forName("android.net.ConnectivityManager");
//                Object mConMObj = context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                Method setEnable = mConM.getMethod("setMobileDataEnabled", boolean.class);
//                setEnable.invoke(mConMObj, enable);// true or false
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 反射调用网络数据开关接口(2016/11/30 修改 for android api22)
     *
     * @param context ：上下文
     * @param enable  ： true 开启; false 关闭
     */
    private static void setMobileDataEnable(Context context, boolean enable) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(tm, enable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置反射Boolean型方法数值
     *
     * @param reflectObj ： 反射对象
     * @param methodName ： 方法名
     * @param enable     ： true or false
     * @return
     * @throws Exception
     */
    private static Object setBooleanInvokeMethod(Object reflectObj, String methodName, boolean enable) throws Exception {
        if (reflectObj == null) {
            return null;
        }
        Class<?> localClass = reflectObj.getClass();
        Class<?>[] arrayOfClass = new Class[1];
        arrayOfClass[0] = Boolean.TYPE;
        Method localMethod = localClass.getMethod(methodName, arrayOfClass);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Boolean.valueOf(enable);
        return localMethod.invoke(reflectObj, arrayOfObject);
    }

    /**
     * 通过反射获取数值
     *
     * @param reflectObj : 反射对象
     * @param methodName : 方法
     * @return
     * @throws Exception
     */
    private static Object getValueInvokeMethod(Object reflectObj, String methodName) throws Exception {
        if (reflectObj == null) {
            return null;
        }
        Method method = reflectObj.getClass().getMethod(methodName);
        if (method != null) {
            Object object = method.invoke(reflectObj);
            if (object != null) {
                return object;
            }
        }
        return null;
    }

    /**
     * 关闭移动网络数据访问功能
     *
     * @param context
     * @return
     */
    public static boolean disableMobileData(Context context) {
        try {
            if (isMobileEnabled(context)) {
                LogUtils.i(tag, "disable mobileData");
                setMobileDataEnable(context, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 启用移动网络数据访问功能
     *
     * @param context
     * @return
     */
    public static boolean enableMobileData(Context context) {
        // ConnectivityManager cm =
        // (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //if (HardWare.isYaxonMobie()) {
        try {
            if (!isMobileEnabled(context)) {
                LogUtils.i(tag, "enable mobileData");
                // setBooleanInvokeMethod(cm, "setMobileDataEnabled", true);
                setMobileDataEnable(context, true);
                Thread.sleep(4000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //}
        return true;
    }

    /**
     * 判断无线网络数据访问功能是否开启
     *
     * @param context
     * @return
     */
    public static boolean isMobileEnabled(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        boolean ret = false;
//        try {
//            ret = (Boolean) getValueInvokeMethod(cm, "getMobileDataEnabled");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return ret;

        boolean mobileDataEnabled = false;
        try {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod) {
                mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return mobileDataEnabled;
    }

    /**
     * 无线网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isMobileAvailable(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        }
        return false;
    }

    /**
     * 网络是否已连接.（没有网络连接则不能登录，并提示用户)
     *
     * @param context
     * @return true, if is connected
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if (networkInfo == null) {
        // enableMobileData(context);
        // networkInfo = cm.getActiveNetworkInfo();
        // }
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断Network是否开启(包括移动网络和wifi)
     *
     * @return
     */
    public static boolean isNetWorkEnabled(Context context) {
        return (isMobileConnected(context) || isWifiConnected(context));
    }

    /**
     * 判断Network是否连接成功(包括移动网络和wifi)
     *
     * @param context 上下文
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        return (isMobileConnected(context) || isWifiConnected(context));
    }

    /**
     * 判断是否是公网或专网
     *
     * @return true为公网, false为专网
     */
    public static boolean isPublicNetWork() {
        boolean flag = false;

        if (NetAPN.CTWAP.equals(mApnName.toLowerCase())
                || NetAPN.CTNET.equals(mApnName.toLowerCase())) {

            flag = true;
        } else if (NetAPN.CMWAP.equals(mApnName.toLowerCase())
                || NetAPN.CMNET.equals(mApnName.toLowerCase())) {

            flag = true;
        } else if (NetAPN.UNIWAP.equals(mApnName.toLowerCase())
                || NetAPN.UNINET.equals(mApnName.toLowerCase())) {

            flag = true;
        } else if (NetAPN.G3WAP.equals(mApnName.toLowerCase())
                || NetAPN.G3NET.equals(mApnName.toLowerCase())) {

            flag = true;
        }
        return flag;
    }

    /**
     * 获取通过Mobile接口接收的字节总数（这里不包含WiFi）
     */
    public static long getMobileRxBytes() {
        if (TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED) {
            return TrafficStats.getMobileRxBytes();
        }
        return 0;
    }

    /**
     * 获取通过Mobile接口接发送的字节总数（这里不包含WiFi）
     */
    public static long getMobileTxBytes() {
        if (TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED) {
            return TrafficStats.getMobileTxBytes();
        }
        return 0;
    }

    /**
     * 获取通过网络接口接收的字节总数（包含Mobile和WiFi）
     */
    public static long getTotalRxBytes() {
        if (TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED) {
            return TrafficStats.getTotalRxBytes();
        }
        return 0;
    }

    /**
     * 获取通过网络接口发送的字节总数（包含Mobile和WiFi）
     */
    public static long getTotalTxBytes() {
        if (TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED) {
            return TrafficStats.getTotalTxBytes();
        }
        return 0;
    }

    /**
     * 获取通过UID网络接口接收的字节总数（包含Mobile和WiFi） uid = Process.myUid()
     */
    public static long getUidRxBytes(int uid) {
        if (TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED) {
            return TrafficStats.getUidRxBytes(uid);
        }
        return 0;
    }

    /**
     * 获取通过UID网络接口发送的字节总数（包含Mobile和WiFi）
     */
    public static long getUidTxBytes(int uid) {
        if (TrafficStats.getMobileRxBytes() != TrafficStats.UNSUPPORTED) {
            return TrafficStats.getUidTxBytes(uid);
        }
        return 0;
    }

    /**
     * 保存应用程序流量至PrefsSys
     */
    public static long getUidTotalBytes() {
        long bytes = getUidRxBytes(Process.myUid()) + getUidTxBytes(Process.myUid());
        LogUtils.d(tag, "flow = " + bytes);
        return bytes;
    }
}

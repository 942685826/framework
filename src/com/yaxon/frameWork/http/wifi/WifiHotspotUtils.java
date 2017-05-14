package com.yaxon.frameWork.http.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.yaxon.frameWork.debug.LogUtils;
import com.yaxon.frameWork.debug.ToastUtils;

import java.lang.reflect.Method;

/**
 * @author guojiaping
 * @version 2017/5/12 创建<br>.
 */
public class WifiHotspotUtils {
    private static final String TAG = WifiHotspotUtils.class.getSimpleName();

    public static final int MESSAGE_AP_STATE_ENABLED = 1;
    public static final int MESSAGE_AP_STATE_FAILED = 2;
    //默认wifi秘密
    private static final String DEFAULT_AP_PASSWORD = "12345678";
    private static WifiHotspotUtils instance;
    private static Handler mHandler;
    private static Context mContext;
    private WifiManager wifiManager;
    //监听WiFi热点的状态变化
    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    public static final int WIFI_AP_STATE_DISABLING = 10;
    public static final int WIFI_AP_STATE_DISABLED = 11;
    public static final int WIFI_AP_STATE_ENABLING = 12;
    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_FAILED = 14;

    public enum WifiSecurityType {
        WIFICIPHER_NOPASS, WIFICIPHER_WPA, WIFICIPHER_WEP, WIFICIPHER_INVALID, WIFICIPHERWAP2
    }

    private WifiHotspotUtils(Context context) {
        mContext = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_AP_STATE_CHANGED_ACTION);
        context.registerReceiver(wifiStateBroadcastReceiver, filter);
    }

    protected void finalize() {
        LogUtils.d(TAG, "finalize");
        mContext.unregisterReceiver(wifiStateBroadcastReceiver);
    }

    public static WifiHotspotUtils getInstance(Context context) {
        if (null == instance) {
            instance = new WifiHotspotUtils(context);
        }
        return instance;
    }

    public boolean turnOnWifiHotspot(String ssid, String password, WifiSecurityType type) {
        //配置热点信息
        WifiConfiguration configuration = new WifiConfiguration();
        configuration.SSID = new String(ssid);
        configuration.networkId = 1;
        configuration.allowedAuthAlgorithms.clear();
        configuration.allowedGroupCiphers.clear();
        configuration.allowedKeyManagement.clear();
        configuration.allowedPairwiseCiphers.clear();
        configuration.allowedProtocols.clear();

        if (type == WifiSecurityType.WIFICIPHER_NOPASS) {
            LogUtils.d(TAG, "wifi hotspot------no password");
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN, true);
            configuration.wepKeys[0] = "";
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        } else if (type == WifiSecurityType.WIFICIPHER_WPA) {
            LogUtils.d(TAG, "wifi hotspot------wpa");
            if (null != password && password.length() >= 8) {
                configuration.preSharedKey = password;
            } else {
                configuration.preSharedKey = DEFAULT_AP_PASSWORD;
            }
            configuration.hiddenSSID = false;
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//            configuration.allowedKeyManagement.set(4);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            configuration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        } else if (type == WifiSecurityType.WIFICIPHERWAP2) {
            LogUtils.d(TAG, "wifi hotspot------wpa2");
            if (null != password && password.length() >= 8) {
                configuration.preSharedKey = password;
            } else {
                configuration.preSharedKey = DEFAULT_AP_PASSWORD;
            }
            configuration.hiddenSSID = true;
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedKeyManagement.set(4);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            configuration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        }
        try {
            Method method = wifiManager.getClass().getMethod("setWifiApConfiguration",
                    configuration.getClass());
            boolean rt = (boolean) method.invoke(wifiManager, configuration);
            LogUtils.d(TAG, " rt = " + rt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return setWifiHotspotEnabled();
    }

    /**
     * 获取热点状态
     *
     * @return
     */
    public int getWifiHotspotState() {
        int state = -1;
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApState");
            state = (int) method.invoke(wifiManager);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        LogUtils.d(TAG, "getWifiAPState.state " + state);
        return state;
    }

    private boolean setWifiHotspotEnabled() {
        //开启热点需要关闭wifi
        while (wifiManager.getWifiState() != WifiManager.WIFI_STATE_DISABLED) {
            wifiManager.setWifiEnabled(false);
            try {
                Thread.sleep(200);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //确保wifi热点关闭
        while (getWifiHotspotState() != WIFI_AP_STATE_DISABLED) {
            try {
                Method method = wifiManager.getClass().getMethod("setWifiApEnabled",
                        WifiConfiguration.class, boolean.class);
                method.invoke(wifiManager, null, false);
                Thread.sleep(200);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //开启wifi热点
        try {
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, null, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 关闭wifi热点
     */
    public void closeWifiHotspot() {
        if (getWifiHotspotState() != WIFI_AP_STATE_DISABLED) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration configuration = (WifiConfiguration) method.invoke(wifiManager);
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, configuration, false);
                LogUtils.d(TAG, "正在关闭");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            LogUtils.d(TAG, "已经关闭");
        }
    }

    public void registerHandler(Handler handler) {
        mHandler = handler;
    }

    public void unRegisterHandler() {
        mHandler = null;
    }

    //监听wifi热点状态变化
    private BroadcastReceiver wifiStateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(TAG, "WifiHotspotUtils onReceive:" + intent.getAction());
            if (WIFI_AP_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(EXTRA_WIFI_AP_STATE, -1);
                if (state == WIFI_AP_STATE_ENABLED) {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(MESSAGE_AP_STATE_ENABLED);
                    }
                }
                if (state == WIFI_AP_STATE_DISABLED || state == WIFI_AP_STATE_FAILED) {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(MESSAGE_AP_STATE_FAILED);
                    }
                }
            }
        }
    };

    /**
     * 获取热点ssid
     *
     * @return
     */
    public String getValidHotspotSsid() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(wifiManager);
            return configuration.SSID;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取热点秘密
     *
     * @return
     */
    public String getValidPassword() {
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(wifiManager);
            return configuration.preSharedKey;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取热点安全类型
     *
     * @return
     */
    public int getValidSecurity() {
        WifiConfiguration configuration = null;
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
            configuration = (WifiConfiguration) method.invoke(wifiManager);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        LogUtils.d(TAG, "getSecurity = " + configuration.allowedKeyManagement);
        if (configuration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE)) {
            return WifiSecurityType.WIFICIPHER_NOPASS.ordinal();
        } else if (configuration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return WifiSecurityType.WIFICIPHER_WPA.ordinal();
        } else if (configuration.allowedKeyManagement.get(4)) {
            return WifiSecurityType.WIFICIPHERWAP2.ordinal();
        }
        return WifiSecurityType.WIFICIPHER_INVALID.ordinal();
    }
}

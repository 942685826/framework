package com.yaxon.frameWork.http.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import com.yaxon.frameWork.debug.LogUtils;
import com.yaxon.frameWork.debug.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guojiaping
 * @version 2017/5/12 创建<br>
 */
public class WifiUtil {
    private final static String TAG = WifiUtil.class.getSimpleName();
    //定义WifiManager对象
    private WifiManager wifiManager;
    //定义WifiInfo对象
    private WifiInfo wifiInfo;
    //扫描出的网络连接列表
    private List<ScanResult> wifiList;
    //网络连接列表
    private List<WifiConfiguration> wifiConfigurationList;
    //定义一个WifiLock
    WifiLock wifiLock;

    public WifiUtil(Context context) {
        //取得WifiManager对象
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //取得wifiInfo对象
        wifiInfo = wifiManager.getConnectionInfo();
    }

    /**
     * 打开WIFI
     *
     * @param context
     */
    public void openWifi(Context context) {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            ToastUtils.showShort(context, "亲，Wifi正在开启，不用再开启了");
        } else {
            ToastUtils.showShort(context, "亲，Wifi已经开启，不用再开启了");
        }
    }

    /**
     * 关闭WIFI
     *
     * @param context
     */
    public void closeWifi(Context context) {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            ToastUtils.showShort(context, "亲，Wifi已经关闭，不用再关闭了");
        } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            ToastUtils.showShort(context, "亲，Wifi正在关闭，不用再关闭了");
        } else {
            ToastUtils.showShort(context, "请重新关闭");
        }
    }

    /**
     * 检查当前WiFi状态
     *
     * @param context
     */
    public void checkWifiState(Context context) {
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            ToastUtils.showShort(context, "WIFI正在关闭");
        } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            ToastUtils.showShort(context, "WIFI已经关闭");
        } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            ToastUtils.showShort(context, "WIFI正在开启");
        } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            ToastUtils.showShort(context, "WIFI已经开启");
        } else {
            ToastUtils.showShort(context, "没有获取到Wifi状态");
        }
    }

    /**
     * 锁定WifiLock
     */
    public void acquireWifiLock() {
        wifiLock.acquire();
    }

    /**
     * 解锁WifiLock
     */
    public void releaseWifiLock() {
        //判断时候锁定
        if (wifiLock.isHeld()) {
            wifiLock.release();
        }
    }

    /**
     * 创建一个WifiLock
     */
    public void createWifiLock() {
        wifiLock = wifiManager.createWifiLock("Test");
    }

    /**
     * 得到配置好的网络
     *
     * @return
     */
    public List<WifiConfiguration> getWifiConfigurationList() {
        return wifiConfigurationList;
    }

    /**
     * 指定配置好的网络进行连接
     *
     * @param index
     */
    public void ConnectConfiguration(int index) {
        if (index >= 0 && index < wifiConfigurationList.size()) {
            //连接配置好的指定ID的网络
            wifiManager.enableNetwork(wifiConfigurationList.get(index).networkId, true);
        }
    }

    public void startScan(Context context) {
        wifiManager.startScan();
        //得到扫描的结果
        List<ScanResult> results = wifiManager.getScanResults();
        //得到配置好的网络连接
        wifiConfigurationList = wifiManager.getConfiguredNetworks();
        if (results == null) {
            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                ToastUtils.showShort(context, "当前区域没有无线网络");
            } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                ToastUtils.showShort(context, "wifi正在开启， 请稍后扫描");
            } else {
                ToastUtils.showShort(context, "Wifi没有开启");
            }
        } else {

            wifiList = new ArrayList<>();
            for (ScanResult result : results) {
                if (result.SSID == null || result.SSID.length() == 0 ||
                        result.capabilities.contains("[IBSS]")) {
                    continue;
                }
                boolean found = false;
                for (ScanResult item : wifiList) {
                    if (item.SSID.equals(result.SSID) && item.capabilities.equals(result.capabilities)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    wifiList.add(result);
                }
            }
            if (wifiList.size() == 0) {
                ToastUtils.showShort(context, "当前区域没有无线网络");
            }
        }
    }

    /**
     * 得到网络列表
     *
     * @return
     */
    public List<ScanResult> getWifiList() {
        return wifiList;
    }

    /**
     * 查看扫描结果
     *
     * @return
     */
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < wifiList.size(); i++) {
            stringBuilder.append("index_" + (i + 1) + ":");
            //将ScanResult信息转换成一个字符串包
            //其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append(wifiList.get(i).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    /**
     * 得到接入点的BSSID
     *
     * @return
     */
    public String getMacAddress() {
        return wifiInfo == null ? "NULL" : wifiInfo.getBSSID();
    }

    /**
     * 得到连接的IP
     *
     * @return
     */
    public int getIPAddress() {
        return wifiInfo == null ? 0 : wifiInfo.getIpAddress();
    }

    /**
     * 得到连接的ID
     *
     * @return
     */
    public int getNetworkId() {
        return wifiInfo == null ? 0 : wifiInfo.getNetworkId();
    }

    /**
     * 得到所有wifiInfo的所有信息包
     *
     * @return
     */
    public String getWifiInfgo() {
        return wifiInfo == null ? "NULL" : wifiInfo.toString();
    }

    /**
     * 添加一个网络并连接
     *
     * @param wifiConfiguration
     */
    public void addNetwork(WifiConfiguration wifiConfiguration, Context context) {
        WifiHotspotUtils.getInstance(context);
        int wcgId = wifiManager.addNetwork(wifiConfiguration);
        boolean enable = wifiManager.enableNetwork(wcgId, true);
    }

    /**
     * 断开指定ID的网络
     *
     * @param netId
     */
    public void disconnectWifi(int netId) {
        wifiManager.disableNetwork(netId);
        wifiManager.disconnect();
    }

    /**
     * 删除值得wifi
     *
     * @param netId
     */
    public void removeWifi(int netId) {
        disconnectWifi(netId);
    }

    /**
     * 创建WiFi热点
     *
     * @param SSID
     * @param password
     * @param type
     * @return
     */
    public WifiConfiguration createWifiInfo(String SSID, String password, int type) {
        WifiConfiguration configuration = new WifiConfiguration();
        configuration.allowedAuthAlgorithms.clear();
        configuration.allowedGroupCiphers.clear();
        configuration.allowedKeyManagement.clear();
        configuration.allowedPairwiseCiphers.clear();
        configuration.allowedProtocols.clear();
        configuration.SSID = "\"" + SSID + "\"";
        WifiConfiguration tempConfiguration = isExists(SSID);
        if (tempConfiguration != null) {
            wifiManager.removeNetwork(tempConfiguration.networkId);
        }

        if (type == 1) {
            configuration.wepKeys[0] = "";
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        }

        if (type == 2) {
            configuration.hiddenSSID = true;
            configuration.wepKeys[0] = "\"" + password + "\"";
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        }

        if (type == 3) {
            configuration.preSharedKey = "\"" + password + "\"";
            configuration.hiddenSSID = true;
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            configuration.status = WifiConfiguration.Status.ENABLED;

        }
        return configuration;
    }

    private WifiConfiguration isExists(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (("\"" + SSID + "\"").equals(existingConfig.SSID)) {
                return existingConfig;
            }
        }
        return null;
    }
}
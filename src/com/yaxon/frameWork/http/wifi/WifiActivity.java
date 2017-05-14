package com.yaxon.frameWork.http.wifi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.debug.LogUtils;

/**
 * @author guojiaping
 * @version 2017/5/14.
 */
public class WifiActivity extends Activity {
    private String TAG = "WifiApActivity";
    private Button mBtStartWifiAp, mBtStopWifiAp;
    private EditText mWifiSsid, mWifiPassword;
    private RadioGroup mRgWifiSerurity;
    private RadioButton mRdNo, mRdWpa, mRdWpa2;
    private TextView mWifiApState;
    private WifiHotspotUtils.WifiSecurityType mWifiType = WifiHotspotUtils.WifiSecurityType.WIFICIPHER_NOPASS;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            LogUtils.d(TAG, "WifiApActivity message.what=" + msg.what);
            switch (msg.what) {
                case WifiHotspotUtils.MESSAGE_AP_STATE_ENABLED:
                    String ssid = WifiHotspotUtils.getInstance(WifiActivity.this).getValidHotspotSsid();
                    String pw = WifiHotspotUtils.getInstance(WifiActivity.this).getValidPassword();
                    int security = WifiHotspotUtils.getInstance(WifiActivity.this).getValidSecurity();
                    mWifiApState.setText("wifi热点开启成功" + "\n"
                            + "SSID = " + ssid + "\n"
                            + "Password = " + pw + "\n"
                            + "Security = " + security);
                    break;
                case WifiHotspotUtils.MESSAGE_AP_STATE_FAILED:
                    mWifiApState.setText("wifi热点关闭");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_ap_test);
        WifiHotspotUtils.getInstance(getApplicationContext());
        WifiHotspotUtils.getInstance(this).registerHandler(mHandler);
        mBtStartWifiAp = (Button) findViewById(R.id.bt_start_wifiap);
        mWifiSsid = (EditText) findViewById(R.id.et_ssid);
        mWifiPassword = (EditText) findViewById(R.id.et_password);
        mRgWifiSerurity = (RadioGroup) findViewById(R.id.rg_security);
        mRdNo = (RadioButton) findViewById(R.id.rd_no);
        mRdWpa = (RadioButton) findViewById(R.id.rd_wpa);
        mRdWpa2 = (RadioButton) findViewById(R.id.rd_wpa2);
        mWifiApState = (TextView) findViewById(R.id.tv_state);
        mBtStopWifiAp = (Button) findViewById(R.id.bt_stop_wifiap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRgWifiSerurity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {

                if (arg1 == mRdNo.getId()) {
                    mWifiType = WifiHotspotUtils.WifiSecurityType.WIFICIPHER_NOPASS;
                } else if (arg1 == mRdWpa.getId()) {
                    mWifiType = WifiHotspotUtils.WifiSecurityType.WIFICIPHER_WPA;
                } else if (arg1 == mRdWpa2.getId()) {
                    mWifiType = WifiHotspotUtils.WifiSecurityType.WIFICIPHERWAP2;
                }
                LogUtils.d(TAG, "radio check mWifiType = " + mWifiType);
            }
        });
        mBtStartWifiAp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String ssid = mWifiSsid.getText().toString();
                String password = mWifiPassword.getText().toString();
                LogUtils.d(TAG, "ssid = " + ssid + "password = " + password);
                if (null == ssid || "".equals(ssid)) {
                    Toast.makeText(WifiActivity.this, "请输入ssid", Toast.LENGTH_SHORT).show();
                    return;
                }
                mWifiApState.setText("正在开启");
                WifiHotspotUtils.getInstance(WifiActivity.this)
                        .turnOnWifiHotspot(ssid, password, mWifiType);

            }
        });

        mBtStopWifiAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiHotspotUtils.getInstance(WifiActivity.this).closeWifiHotspot();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtils.d(TAG, "WifiApActivity onBackPressed");
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, "WifiApActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "WifiApActivity onDestroy");
        WifiHotspotUtils.getInstance(this).unRegisterHandler();
    }
}

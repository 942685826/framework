package com.yaxon.frameWork.common;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.yaxon.frameWork.receiver.ForceOfflineReceiver;

/**
 * Created by GJP on 2017/9/7.
 */
public abstract class BaseActivity extends Activity {
    private ForceOfflineReceiver receiver;
    private boolean isAllowFullScreen = false;
    private boolean isKeyDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(savedInstanceState);
        if (isAllowFullScreen) {
            //取消标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //取消状态栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        ActivityController.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.yaxon.frameWork.receiver.FORCE_OFFLINE");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver, intentFilter);
        //还可以再此添加数据统计
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public abstract void initContentView(Bundle savedInstanceState);

    public <T extends View> T findViewByIds(int id) {
        return (T) findViewById(id);
    }

    protected void finishAll() {
        ActivityController.finishAll();
    }

    protected void setKeyDown(boolean isKeyDown) {
        this.isKeyDown = isKeyDown;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isKeyDown) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // 创建退出对话框
                AlertDialog isExit = new AlertDialog.Builder(this).create();
                // 设置对话框标题
                isExit.setTitle("温馨提示");
                // 设置对话框消息
                isExit.setMessage("主人，确定要离开吗？");
                // 添加选择按钮并注册监听
                isExit.setButton("确定", listener);
                isExit.setButton2("取消", listener);
                // 显示对话框
                isExit.show();
            }
        } else {
            finish();
        }
        return false;
    }

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finishAll();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
}

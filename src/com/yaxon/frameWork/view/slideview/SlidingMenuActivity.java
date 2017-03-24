package com.yaxon.frameWork.view.slideview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.debug.ToastUtils;

/**
 * 模仿QQ侧滑
 *
 * @author guojiaping
 * @version 2016-8-4 创建<br>
 */
public class SlidingMenuActivity extends Activity implements View.OnClickListener {
    private SlidingMenu mMenu;
    private RelativeLayout rl_1, rl_2, rl_3, rl_4, rl_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题
        setContentView(R.layout.slide_menu_layout);
        initView();
        initEvent();
    }

    private void initView() {
        mMenu = (SlidingMenu) findViewById(R.id.id_menu);
        rl_1 = (RelativeLayout) findViewById(R.id.rl_1);
        rl_2 = (RelativeLayout) findViewById(R.id.rl_2);
        rl_3 = (RelativeLayout) findViewById(R.id.rl_3);
        rl_4 = (RelativeLayout) findViewById(R.id.rl_4);
        rl_5 = (RelativeLayout) findViewById(R.id.rl_5);
    }

    private void initEvent() {
        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
        rl_4.setOnClickListener(this);
        rl_5.setOnClickListener(this);
    }

    public void toggleMenu(View view) {
        mMenu.toggle();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_1:
                ToastUtils.showShort(SlidingMenuActivity.this, "开通会员");
                break;
            case R.id.rl_2:
                ToastUtils.showShort(SlidingMenuActivity.this, "QQ钱包");
                break;

            case R.id.rl_3:
                ToastUtils.showShort(SlidingMenuActivity.this, "网上营业厅");
                break;

            case R.id.rl_4:
                ToastUtils.showShort(SlidingMenuActivity.this, "个性装扮");
                break;

            case R.id.rl_5:
                ToastUtils.showShort(SlidingMenuActivity.this, "我的收藏");
                break;

        }
    }
}

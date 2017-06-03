package com.yaxon.frameWork.view.editText;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.debug.ToastUtils;

/**
 * @author guojiaping
 * @version 2017/6/3 创建<br>.
 */
public class ClearEditTextActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clear_edittext_layout);
        final ClearEditText username = (ClearEditText) findViewById(R.id.username);
        final ClearEditText password = (ClearEditText) findViewById(R.id.password);

        ((Button) findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username.getText())) {
                    //设置晃动
                    username.setShakeAnimation();
                    //设置提示
                    ToastUtils.showShort(ClearEditTextActivity.this, "用户名不能为空");
                    return;
                }

                if (TextUtils.isEmpty(password.getText())) {
                    password.setShakeAnimation();
                    ToastUtils.showShort(ClearEditTextActivity.this, "密码不能为空");
                    return;
                }
            }
        });
    }
}

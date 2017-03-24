package com.yaxon.frameWork.view.popupView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.debug.ToastUtils;


public class Android_UI_16Activity extends Activity {

    private static final int ID_USER = 1;
    private static final int ID_GROUNP = 2;
    private PopupJar mPopup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_layout);

        initData();
        initEvent();
    }

    private void initData(){
        PopupItem userItem = new PopupItem(ID_USER, "用户", getResources().getDrawable(R.drawable.child_image));
        PopupItem grounpItem = new PopupItem(ID_GROUNP, "群组", getResources().getDrawable(R.drawable.user_group));
        userItem.setSticky(true);

        mPopup = new PopupJar(this, PopupJar.VERTICAL);
        mPopup.addPopuItem(userItem);
        mPopup.addPopuItem(grounpItem);
    }

    private void initEvent(){

        mPopup.setOnPopuItemClickListener(new PopupJar.OnPopuItemClickListener() {
            @Override
            public void onItemClick(PopupJar source, int pos, int actionId) {
                PopupItem PopupItem = mPopup.getPopuItem(pos);

                if (actionId == ID_USER) {
                    ToastUtils.showLong(getApplication(), "click user");
                } else if (actionId == ID_GROUNP) {
                    ToastUtils.showLong(getApplication(), "click group");
                } else {
                    ToastUtils.showLong(getApplication(), PopupItem.getTitle() + " selected");
                }
            }
        });
        mPopup.setOnDismissListener(new PopupJar.OnDismissListener() {
            @Override
            public void onDismiss() {
                ToastUtils.showLong(getApplication(), "Dismissed");
            }
        });

        Button btn1 = (Button) this.findViewById(R.id.btn1);
        Button btn2 = (Button) this.findViewById(R.id.btn2);
        Button btn3 = (Button) this.findViewById(R.id.btn3);
        Button btn4 = (Button) this.findViewById(R.id.btn4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.show(v);
            }
        });

        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.show(v);
            }
        });

        btn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.show(v);
                mPopup.setAnimStyle(PopupJar.ANIM_REFLECT);
            }
        });

        btn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Android_UI_16Activity.this, ListDemoActivity.class);
                startActivity(intent);
            }
        });
    }
}
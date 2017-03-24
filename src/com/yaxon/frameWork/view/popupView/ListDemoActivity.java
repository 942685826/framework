package com.yaxon.frameWork.view.popupView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.adapter.CommonAdapter;
import com.yaxon.frameWork.adapter.CommonViewHold;
import com.yaxon.frameWork.debug.ToastUtils;

public class ListDemoActivity extends Activity {

    private int mSelectedRow = 0;

    private ImageView mMoreIv = null;

    private static final int ID_USER = 1;
    private static final int ID_GROUP = 2;
    private ListView mList;
    private PopupJar mPopupJar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_list);
        mList = (ListView) findViewById(R.id.l_list);

        initData();
        initEvent();

    }

    private void initEvent() {
        mPopupJar.setOnPopuItemClickListener(new PopupJar.OnPopuItemClickListener() {
            @Override
            public void onItemClick(PopupJar PopupJar, int pos, int actionId) {
                PopupItem PopupItem = PopupJar.getPopuItem(pos);
                if (actionId == ID_USER) {
                    ToastUtils.showShort(getApplicationContext(), "Add item selected on row " + mSelectedRow);
                } else {
                    ToastUtils.showShort(getApplicationContext(), PopupItem.getTitle() + " item selected on row "
                            + mSelectedRow);
                }
            }
        });

        mPopupJar.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mMoreIv.setImageResource(R.drawable.ic_list_more);
            }
        });

        mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedRow = position;
                mPopupJar.show(view);

                mMoreIv = (ImageView) view.findViewById(R.id.i_more);
                mMoreIv.setImageResource(R.drawable.ic_list_more_selected);
            }
        });
    }

    private void initData(){
        CommonAdapter adapter = new CommonAdapter<String>(getModelData(), this, R.layout.popup_listitem) {
            @Override
            public void convert(CommonViewHold commonViewHold, String item) {
                commonViewHold.setText(R.id.t_name, item);
            }
        };
        mList.setAdapter(adapter);

        PopupItem addItem = new PopupItem(ID_USER, "user", getResources().getDrawable(R.drawable.child_image));
        PopupItem acceptItem = new PopupItem(ID_GROUP, "group", getResources().getDrawable(R.drawable.user_group));

        mPopupJar = new PopupJar(this, PopupJar.HORIZONTAL);
        mPopupJar.addPopuItem(addItem);
        mPopupJar.addPopuItem(acceptItem);

    }

    private String[] getModelData(){
        String[] data = {"Test 01", "Test 02", "Test 03", "Test 04", "Test 05", "Test 06", "Test 07", "Test 08",
                "Test 09", "Test 10"};
        return data;
    }
}
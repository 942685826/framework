package com.yaxon.frameWork.view.date;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.view.date.wheelwidge.NumericWheelAdapter;
import com.yaxon.frameWork.view.date.wheelwidge.OnWheelClickedListener;
import com.yaxon.frameWork.view.date.wheelwidge.WheelView;

import java.util.Arrays;
import java.util.List;

/**
 * 仿Ios底部时间选择
 *
 * @author guojiaping
 * @version 2016-10-28 创建<br>
 */
public class DateDialog extends Dialog implements
        View.OnClickListener {
    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void refreshPriorityUI(String date, String monh, String year, String day,
                               String hours, String mins);
    }

    private PriorityListener lis;
    private boolean scrolling = false;
    private Context context = null;
    private NumericWheelAdapter day_adapter = null;
    private NumericWheelAdapter hours_adapter = null;
    private NumericWheelAdapter mon_adapter = null;
    private NumericWheelAdapter mins_adapter = null;
    private int curYear = 0;
    private int curMon = 0;
    private int curDay = 0;
    private int curWeek = 0;
    private int mins = 0;
    private static int theme = R.style.myDialog;// 主题
    private int width, height;// 对话框宽高
    private String title;
    private int maxDate;
    private boolean isOld;

    public DateDialog(final Context context,
                      final PriorityListener listener, int currentyear, int currentmonth,
                      int currentday, int mins, int width,
                      int height, String title, int week, boolean isOld) {
        super(context, theme);
        this.context = context;
        lis = listener;
        this.curYear = currentyear;
        this.curMon = currentmonth;
        this.curDay = currentday;
        this.curWeek = week;
        this.width = width;
        this.title = "选择预约时间";
        this.mins = mins;
        this.height = height;
        this.isOld = isOld;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_select_wheel);
        updateDays(curYear, curMon);
        Button btn_sure = (Button) findViewById(R.id.confirm_btn);
        btn_sure.setOnClickListener(this);
        Button btn_cancel = (Button) findViewById(R.id.cancel_btn);
        btn_cancel.setOnClickListener(this);
        LinearLayout date_layout = (LinearLayout) findViewById(R.id.date_selelct_layout);
        LayoutParams lparams_hours = new LayoutParams(width, height / 3 + 10);
        date_layout.setLayoutParams(lparams_hours);
        TextView title_tv = (TextView) findViewById(R.id.diaolog_title_tv);
        title_tv.setText(title);
        WheelView dayview = (WheelView) findViewById(R.id.day);
        WheelView hoursview = (WheelView) findViewById(R.id.hours);
        WheelView minsview = (WheelView) findViewById(R.id.mins);
        OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(WheelView wheel, int itemIndex) {

                wheel.setCurrentItem(itemIndex, true);
            }
        };
        dayview.addClickingListener(click);
        hoursview.addClickingListener(click);
        minsview.addClickingListener(click);
        day_adapter = new NumericWheelAdapter("今天", curWeek, 3, isOld);
        dayview.setAdapter(day_adapter);
        dayview.setCurrentItem(0);
        dayview.setCyclic(false);
        dayview.setVisibleItems(5);
        hours_adapter = new NumericWheelAdapter(0, 24, "%02d", 2);
        hoursview.setAdapter(hours_adapter);
        hoursview.setCurrentItem(0);
        hoursview.setCyclic(false);
        hoursview.setVisibleItems(5);
        mins_adapter = new NumericWheelAdapter(0, 30, "%02d", 1);
        minsview.setAdapter(mins_adapter);
        minsview.setCurrentItem(mins);
        minsview.setCyclic(false);
        minsview.setVisibleItems(5);
    }

    /**
     * 根据年份和月份来更新日期
     */
    private void updateDays(int year, int month) {
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);
        if (list_big.contains(String.valueOf(month))) {
            maxDate = 31;
        } else if (list_little.contains(String.valueOf(month))) {
            maxDate = 30;
        } else {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                maxDate = 29;
            else
                maxDate = 28;
        }
    }

    public DateDialog(Context context, PriorityListener listener) {
        super(context, theme);
        this.context = context;
    }

    public DateDialog(Context context, String birthDate) {
        super(context, theme);
        this.context = context;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_btn:
                String date;
                String year;
                String month;
                int item = day_adapter.getDate();
                if (curDay + item > maxDate) {
                    date = String.format("%02d", (curDay + item) - maxDate);
                    if (curMon == 12) {
                        month = "01";
                        year = String.valueOf(curYear + 1);
                    } else {
                        year = String.valueOf(curYear);
                        month = String.format("%02d", curMon + 1);
                    }
                } else {

                    date = String.format("%02d", curDay + item);
                    year = String.valueOf(curYear);
                    month = String.format("%02d", curMon);
                }
                lis.refreshPriorityUI(
                        date, month, year,
                        day_adapter.getValues(),
                        hours_adapter.getValues(), mins_adapter.getValues());

                this.dismiss();
                break;
            case R.id.cancel_btn:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

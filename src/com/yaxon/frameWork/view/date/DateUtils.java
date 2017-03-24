package com.yaxon.frameWork.view.date;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.utils.ScreenUtils;

import java.util.Calendar;

/**
 * 弹出时间选择框
 *
 * @author guojiaping 2016-10-28 创建<br>
 */
public class DateUtils {

    public static void choseDateAndTime(Context context) {
        int width = ScreenUtils.getScreenWidth(context);
        int height = ScreenUtils.getScreenHeight(context);
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        final int year2 = c.get(Calendar.YEAR);
        final int month2 = c.get(Calendar.MONTH);
        final int date2 = c.get(Calendar.DATE);
        final int todayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        DateDialog dateDialog = new DateDialog(context, new DateDialog.PriorityListener() {
            @Override
            public void refreshPriorityUI(String date, String monh, String year, String day, String hours, String mins) {

            }
        }, year2, month2 + 1, date2, 0, width, height, "选择预约时间", todayOfWeek, false);
        Window window = dateDialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialogstyle); // 添加动画
        dateDialog.setCancelable(true);
        dateDialog.show();
    }
}

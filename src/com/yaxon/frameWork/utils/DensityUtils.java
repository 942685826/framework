package com.yaxon.frameWork.utils;

import android.content.Context;

/**
 * 分辨率修改
 *
 * @author guojiaping
 * @version 2016-11-9 创建<br>
 */
public class DensityUtils {
    private DensityUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 根据手机的分辨率从dip的单位转成为px（像素）
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int qip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px(像素)的单位转成为dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}

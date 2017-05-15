package com.yaxon.frameWork.view.image;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 图片的处理工具类，可以对图片进行压缩，转化为文件等功能
 *
 * @author guojiaping
 * @version 2015/1/17 创建<br>
 */


public class ImageUtil {

    public final static int SMALL_MOD = 1;
    public final static int BIG_MOD = 2;

    /**
     * 这个方法仅仅为了这个项目而存在，定制的压缩方法
     *
     * @param imgPath 图片路径
     * @return 压缩后的Bitmap
     */
    public static Bitmap defineCompressBitmap(String imgPath, int mode) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.d("MaxMemory", maxMemory + "KB");
        if (maxMemory <= 51200) {
            if (mode == 1) {
                return new ImageUtil().compressBitmap(imgPath, 138, 110);
            } else {
                return new ImageUtil().compressBitmap(imgPath, 236, 189);
            }
        } else if (maxMemory <= 76800) {
            if (mode == 1) {
                return new ImageUtil().compressBitmap(imgPath, 165, 132);
            } else {
                return new ImageUtil().compressBitmap(imgPath, 275, 220);
            }
        }
        if (mode == 1) {
            return new ImageUtil().compressBitmap(imgPath, 150, 120);
        } else {
            return new ImageUtil().compressBitmap(imgPath, 275, 220);
        }

    }


    /**
     * 压缩图片(按需计算比例)
     */
    public Bitmap compressBitmap(String imgPath, int realW, int realH) {
        BitmapFactory.Options ops = new BitmapFactory.Options();
        //设置只获取大小信息不获得实际内容
        ops.inJustDecodeBounds = true;
        //获得图片大小信息
        BitmapFactory.decodeFile(imgPath, ops);
        //获得压缩比
        ops.inSampleSize = calculateInSampleSize(ops, realW, realH);
        ops.inPreferredConfig = Bitmap.Config.RGB_565;
        //设置回获得全部信息的状态
        ops.inJustDecodeBounds = false;
        //完整解析成bitmap对象
        return BitmapFactory.decodeFile(imgPath, ops);
    }

    /**
     * 压缩图片(固定倍数)
     */
    public Bitmap compressBitmapForMultiple(String imgPath, int limitW, int limitH, int multiple) {
        BitmapFactory.Options ops = new BitmapFactory.Options();
        //设置只获取大小信息不获得实际内容
        ops.inJustDecodeBounds = true;
        //获得图片大小信息
        BitmapFactory.decodeFile(imgPath, ops);
        //获得压缩比
        ops.inSampleSize = calculateInSampleSizeForMultiple(ops, limitW, limitH, multiple);
        //设置回获得全部信息的状态
        ops.inJustDecodeBounds = false;
        //完整解析成bitmap对象
        return BitmapFactory.decodeFile(imgPath, ops);
    }


    /**
     * 计算压缩比（给定压缩后的尺寸计算动态压缩比）
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 计算压缩比（给定限制尺寸固定倍数压缩）
     * 反复压缩直到达到限制值以下
     */
    public static int calculateInSampleSizeForMultiple(BitmapFactory.Options options, int limitWidth, int limitHeight, int multipe) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 0;
        if (height > limitHeight || width > limitWidth) {

            while (height > limitHeight || width > limitWidth) {
                height /= multipe;
                width /= multipe;
                inSampleSize += multipe;
            }
            return inSampleSize;
        } else {
            return 1;
        }
    }
}

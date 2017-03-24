package com.yaxon.frameWork.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author guojiaping
 * @author 2015-8-1 创建<br>
 */
public class CommonViewHold {
    private View mCommonView;
    private SparseArray<View> mViews;//控件缓存

    public CommonViewHold(Context context, ViewGroup parent, int layoutId) {
        mViews = new SparseArray<>();
        mCommonView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mCommonView.setTag(this);
    }

    public static CommonViewHold getCommonViewHold(Context context, ViewGroup parent, int layoutId, View convertView) {
        if (convertView == null) {
            return new CommonViewHold(context, parent, layoutId);
        }
        return (CommonViewHold) convertView.getTag();
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mCommonView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    //设置text内容
    public CommonViewHold setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    //根据bitmap设置图片
    public CommonViewHold setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    //根据id设置图片
    public CommonViewHold setImageResource(int viewId, int drawableId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(drawableId);
        return this;
    }


    public View getCommonView() {
        return mCommonView;
    }
}

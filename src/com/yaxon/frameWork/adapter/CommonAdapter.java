package com.yaxon.frameWork.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 万能的适配器
 *
 * @author guojiaping
 * @author 2015-8-1 创建<br>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    private List<T> list;//数据源
    private Context context;//上下文
    private T[] dataArray;//数据源（与list二选一）
    private int layoutId;//布局文件

    public CommonAdapter(List<T> list, Context context, int layoutId) {
        this.list = list;
        this.context = context;
        this.layoutId = layoutId;
    }

    public CommonAdapter(T[] dataArray, Context context, int layoutId) {
        this.dataArray = dataArray;
        this.context = context;
        this.layoutId = layoutId;
    }

    public void setListData(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setDataArray(T[] dataArray) {
        this.dataArray = dataArray;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return dataArray.length;
        }
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else {
            return dataArray[position];
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHold commonViewHold = CommonViewHold.getCommonViewHold(context, parent, layoutId, convertView);
        convert(commonViewHold, (T) getItem(position));//函数回调
        return commonViewHold.getCommonView();
    }

    public abstract void convert(CommonViewHold commonViewHold, T item);
}

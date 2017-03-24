package com.yaxon.frameWork.view.image.cycleView;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class CycleViewAdapter extends PagerAdapter {
    private List<View> list;

    public CycleViewAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size() > 1 ? Integer.MAX_VALUE : list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public Object instantiateItem(ViewGroup container, int position) {
        int currentItem = position % list.size();
        ImageView iv = (ImageView) list.get(currentItem);
        try {
            container.addView(iv, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.size() > 1 ? list.get(position % list.size()) : list.get(0);

    }

    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup arg0) {

    }

    @Override
    public void finishUpdate(ViewGroup arg0) {

    }

}
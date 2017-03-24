package com.yaxon.frameWork.view.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import com.yaxon.frameWork.MainActivity;
import com.yaxon.frameWork.R;


import java.util.ArrayList;

/**
 * 首页导航栏
 *
 * @author guojiaping
 * @version 2016/1/5 创建<br>
 */
public class GuideView extends Activity {
    private ViewPager mViewPager;
    private int currIndex = 0;
    private ArrayList<View> views;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);
        initView();

        mViewPager.setAdapter(new MyAdapter());
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);


        //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.guide_view1, null);
        View view2 = mLi.inflate(R.layout.guide_view2, null);
        View view3 = mLi.inflate(R.layout.guide_view3, null);

        //每个页面的view数据
        views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);

        //每一个也没得标题
        titles = new ArrayList<String>();
        titles.add("①");
        titles.add("②");
        titles.add("③");
        titles.add("④");
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
    }

    ;

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageSelected(int arg0) {//参数arg0为选中的View
            Animation animation = getAnimation(currIndex, arg0);
            currIndex = arg0;//设置当前View
            animation.setFillAfter(true);// True:设置图片停在动画结束位置
            animation.setDuration(300);//设置动画持续时间
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    }

    private Animation getAnimation(int currIndex, int arg0) {
        Animation animation = null;
        if (currIndex == arg0 - 1) {//如果滑动到上一个View
            animation = new TranslateAnimation(arg0 - 1, arg0, 0, 0); //圆点移动效果动画，从当前View移动到下一个View
        } else if (currIndex == arg0 + 1) {//圆点移动效果动画，从当前View移动到下一个View，下同。
            animation = new TranslateAnimation(arg0 + 1, arg0, 0, 0);
        }
        return animation;
    }

    public void startbutton(View v) {
        Intent intent = new Intent(GuideView.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }
}

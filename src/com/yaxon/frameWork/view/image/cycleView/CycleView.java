package com.yaxon.frameWork.view.image.cycleView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.view.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 图片轮播
 *
 * @author guojiaping
 * @version 2016/12/27 创建<br>
 */
public class CycleView extends Activity {
    private List<View> mSlideViews;
    private ViewPager mViewPager;
    private ViewGroup group;
    private Handler mHandler;
    private Timer timer;
    private boolean isContinue = true;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private static final int initPositon = 10000;
    private static int currentPosition = initPositon;
    private ImageView[] tips;

    private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
            "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
            "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};

    private String[] imageUrls2 = {
            "http://down1.sucaitianxia.com/psd02/psd158/psds27988.jpg",
            "http://pic2.ooopic.com/11/35/98/12bOOOPIC8f.jpg",
            "http://down1.sucaitianxia.com/psd02/psd158/psds28266.jpg",
            "http://pic02.sosucai.com/PSD/PSD_cd0267/b/PSD_cd0267_00016.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cycle_view_layout);
        mSlideViews = new ArrayList<>();
        int[] slidesBeans = new int[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4, R.drawable.banner5,};
        for (int i = 0; i < slidesBeans.length; i++) {
            ImageView itemView = new ImageView(this);
            int W = getWindowManager().getDefaultDisplay().getWidth();//获取屏幕高度
            ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(W, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(param);
            itemView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageLoader.loadImage(imageUrls[i], itemView);
            itemView.setImageResource(slidesBeans[i]);
            mSlideViews.add(itemView);
        }
        group = (ViewGroup) findViewById(R.id.viewGroup);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOnTouchListener(new MyTouchListener());

        mViewPager.setAdapter(new CycleViewAdapter(mSlideViews));
        mViewPager.setCurrentItem(initPositon);
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
        point();
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(4000);
                            if (isContinue) {
                                mHandler.sendEmptyMessageDelayed(0, 2000);
                                currentPosition++;
                            }
                        } catch (Exception e) {
                            break;
                        }
                    }
                }
            }, 2000);
        }

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                mViewPager.setCurrentItem(currentPosition);
            }
        };
    }

    private void point() {
        // 将点点加入到ViewGroup中
        tips = new ImageView[mSlideViews.size()];
        if (group != null) {
            group.removeAllViews();
        }
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12,
                    12);
            params.setMargins(0, 0, 12, 12);
            imageView.setLayoutParams(params);
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            group.addView(imageView);
        }
    }

    class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    isContinue = false;
                    break;
                case MotionEvent.ACTION_UP:
                    isContinue = true;
                    break;
                default:
                    break;
            }
            return false;
        }

    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            setImageBackground(position % mSlideViews.size());
            currentPosition = position;
        }

        private void setImageBackground(int selectItems) {
            for (int i = 0; i < tips.length; i++) {
                if (i == selectItems) {
                    tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
                } else {
                    tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub

        }
    }
}
